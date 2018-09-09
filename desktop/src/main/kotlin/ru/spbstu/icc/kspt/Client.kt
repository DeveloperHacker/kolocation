package ru.spbstu.icc.kspt


object Client {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = ArrayList<Status>()
        SocketWrapper.create(Configuration.SERVER_IP, Configuration.SERVER_PORT) {
            for (i in 0..100) {
                writeln(Command.READ.toString())
                data.add(Status.valueOf(readln()))
            }
            writeln(Command.CLOSE.toString())
        }
        data.forEach { println(data) }
    }
}