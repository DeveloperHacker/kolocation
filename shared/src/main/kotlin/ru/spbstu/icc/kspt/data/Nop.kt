package ru.spbstu.icc.kspt.data

object Nop {
    fun dump() = "Nop()"

    fun valueOf(data: String): Nop? = if (data.removeSurrounding("Nop(", ")").isEmpty()) Nop else null
}