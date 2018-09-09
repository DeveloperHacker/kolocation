package ru.spbstu.icc.kspt


class Logger {
    fun info(message: Any? = "") {
        System.out.println("INFO: $message")
        System.out.flush()
    }

    fun warn(message: Any? = "") {
        System.err.println("WARN: $message")
        System.err.flush()
    }

    fun error(message: Any? = "") {
        System.err.println("ERROR: $message")
        System.err.flush()
    }
}