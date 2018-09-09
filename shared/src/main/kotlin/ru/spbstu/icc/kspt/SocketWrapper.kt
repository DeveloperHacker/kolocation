package ru.spbstu.icc.kspt

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class SocketWrapper(private val socket: Socket) {
    private val input = BufferedReader(InputStreamReader(socket.getInputStream()))
    private val output = PrintWriter(socket.getOutputStream())

    fun close() = socket.close()

    fun ready() = input.ready()

    fun readln() = input.readLine() ?: throw ClosedSocketException()

    fun writeln(string: String) {
        output.write(string)
        output.write("\n")
        output.flush()
    }

    class ClosedSocketException : Exception()

    companion object {
        fun <R> create(address: String, port: Int, apply: SocketWrapper.() -> R): R =
                Socket(address, port).use { SocketWrapper(it).apply() }
    }
}
