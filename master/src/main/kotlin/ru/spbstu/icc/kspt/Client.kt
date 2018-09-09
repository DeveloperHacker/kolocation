package ru.spbstu.icc.kspt


object Client {
    @JvmStatic
    fun main(args: Array<String>) {
        val logger = Logger()
        SocketWrapper.create(Configuration.SERVER_IP, Configuration.SERVER_PORT) {
            logger.info("Connect to the server $canonicalHostName")
            writeln(Command.SHUTDOWN.toString())
            logger.info("Disconnect from the server $canonicalHostName")
        }
    }
}
