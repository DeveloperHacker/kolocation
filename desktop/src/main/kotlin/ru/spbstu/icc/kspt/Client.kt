package ru.spbstu.icc.kspt

import ru.spbstu.icc.kspt.data.Nop
import ru.spbstu.icc.kspt.data.Status


object Client {
    @JvmStatic
    fun main(args: Array<String>) {
        val logger = Logger()
        val data = ArrayList<Status>()
        val frequency = Configuration.GENERATION_FREQUENCY // Гц
        val points = Configuration.GENERATION_STEPS
        SocketWrapper.create(Configuration.SERVER_IP, Configuration.SERVER_PORT) {
            logger.info("Connect to the server $canonicalHostName")
            while (data.size < points) {
                writeln(Command.READ.toString())
                while (true) {
                    val datum = readln()
                    if (Nop.valueOf(datum) != null) break
                    data.add(Status.valueOf(datum))
                    logger.info("Loading complete on ${data.size * 100 / points}%")
                }
                Thread.sleep(1000L / frequency / 2)
            }
            writeln(Command.CLOSE.toString())
            logger.info("Disconnect from the server $canonicalHostName")
        }
        data.forEach { println(it) }
    }
}