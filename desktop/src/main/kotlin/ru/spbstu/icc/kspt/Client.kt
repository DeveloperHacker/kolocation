package ru.spbstu.icc.kspt


object Client {
    @JvmStatic
    fun main(args: Array<String>) {
        val logger = Logger()
        val data = ArrayList<Status>()
        SocketWrapper.create(Configuration.SERVER_IP, Configuration.SERVER_PORT) {
            logger.info("Connect to the server $canonicalHostName")
            for (i in 0..100) {
                writeln(Command.READ.toString())
                data.add(Status.valueOf(readln()))
                logger.info("Loading complete on ${i * 100 / 100}%")
            }
            writeln(Command.CLOSE.toString())
            logger.info("Disconnect from the server $canonicalHostName")
        }
        data.forEach { println(it) }
    }
}