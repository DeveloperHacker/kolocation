package ru.spbstu.icc.kspt

import java.io.PrintWriter
import java.net.ServerSocket
import java.util.Date

/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
object Server {

    /**
     * Runs the server.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val listener = ServerSocket(Configuration.SERVER_PORT)
        try {
            while (true) {
                val socket = listener.accept()
                try {
                    val out = PrintWriter(socket.getOutputStream(), true)
                    out.println(Date().toString())
                } finally {
                    socket.close()
                }
            }
        } finally {
            listener.close()
        }
    }
}