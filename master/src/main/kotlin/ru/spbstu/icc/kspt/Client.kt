package ru.spbstu.icc.kspt


object Client {
    @JvmStatic
    fun main(args: Array<String>) {
        SocketWrapper.create(Configuration.SERVER_IP, Configuration.SERVER_PORT) {
            writeln(Command.SHUTDOWN.toString())
        }
    }
}
