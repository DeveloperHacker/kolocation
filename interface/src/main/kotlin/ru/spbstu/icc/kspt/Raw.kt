package ru.spbstu.icc.kspt

data class Raw(val signal1: Double, val signal2: Double) {
    fun dump() = "Raw($signal1,$signal2)"

    companion object {
        fun valueOf(data: String): Raw {
            val (signal1, signal2) = data.removeSurrounding("Raw(", ")").split(',')
            return Raw(signal1.toDouble(), signal2.toDouble())
        }
    }
}