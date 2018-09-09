package ru.spbstu.icc.kspt


object Client {
    class Sinus(private var x: Double, private val dx: Double) {
        fun next() = Math.sin(x).apply {
            x += dx
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val logger = Logger()
        val fq = Configuration.FREQUENCY // Гц
        val points = fq * 10
        val dx = 2 * Math.PI / points
        val sin = Sinus(0.0, dx)
        val cos = Sinus(Math.PI / 2.0, dx)
        SocketWrapper.create(Configuration.SERVER_IP, Configuration.SERVER_PORT) {
            logger.info("Connect to the server $canonicalHostName")
            for (i in 0..points) {
                writeln(Command.WRITE.toString())
                writeln(Raw(sin.next(), cos.next()).dump())
                if (i % fq == 0) logger.info("Uploading complete on ${i * 100 / points}%")
                Thread.sleep(1000L / fq)
            }
            writeln(Command.CLOSE.toString())
            logger.info("Disconnect from the server $canonicalHostName")
        }
    }
}