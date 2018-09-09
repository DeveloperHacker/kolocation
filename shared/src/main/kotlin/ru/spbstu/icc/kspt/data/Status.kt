package ru.spbstu.icc.kspt.data

data class Status(val tag: Tag, val signal1: Double, val signal2: Double) {
    fun dump() = "Status($tag,$signal1,$signal2)"

    enum class Tag { DEAD, ILL, LIVE }

    companion object {
        fun valueOf(data: String): Status {
            val (tag, signal1, signal2) = data.removeSurrounding("Status(", ")").split(',')
            return Status(Tag.valueOf(tag), signal1.toDouble(), signal2.toDouble())
        }
    }
}