package ru.spbstu.icc.kspt

import ru.spbstu.icc.kspt.data.Nop
import ru.spbstu.icc.kspt.data.Raw
import ru.spbstu.icc.kspt.data.Status
import kotlin.collections.ArrayList

class DataHandler {
    private val data = ArrayList<Raw>()

    fun writeRaw(string: String) {
        data.add(Raw.valueOf(string))
    }

    fun readStatus(): List<String> {
        val result = ArrayList<String>()
        while (data.size >= Configuration.GENERATION_FREQUENCY) {
            val values = data.take(Configuration.GENERATION_FREQUENCY)
            data.subList(0, values.size).clear()
            val signal1 = values.map { it.signal1 }.fold(0.0) { acc, it -> acc + it } / values.size
            val signal2 = values.map { it.signal2 }.fold(0.0) { acc, it -> acc + it } / values.size
            val tag = when {
                signal1 < 0 || signal2 < 0 -> Status.Tag.DEAD
                signal1 > signal2 -> Status.Tag.ILL
                else -> Status.Tag.LIVE
            }
            result.add(Status(tag, signal1, signal2).dump())
        }
        result.add(Nop.dump())
        return result
    }
}