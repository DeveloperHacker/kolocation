package ru.spbstu.icc.kspt

import java.net.ServerSocket

object Server {
    @JvmStatic
    fun main(args: Array<String>) {
        val dataHandler = DataHandler()
        ServerSocket(Configuration.SERVER_PORT).use { listener ->
            while (true) {
                listener.acceptWrapped {
                    var close = false
                    while (!close) {
                        val command = Command.valueOf(readln())
                        when (command) {
                            Command.WRITE -> dataHandler.writeRaw(readln())
                            Command.READ -> writeln(dataHandler.readStatus())
                            Command.CLOSE -> close = true
                        }
                    }
                }
            }
        }
    }
}