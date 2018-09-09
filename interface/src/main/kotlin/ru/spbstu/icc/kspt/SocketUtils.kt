package ru.spbstu.icc.kspt

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket

class Socket(socket: java.net.Socket) {
    private val input = BufferedReader(InputStreamReader(socket.getInputStream()))
    private val output = PrintWriter(socket.getOutputStream())

    fun readln() = input.readLine() ?: throw ClosedSocketException()

    fun writeln(string: String) {
        output.write(string)
        output.write("\n")
        output.flush()
    }

    class ClosedSocketException : Exception()

    companion object {
        fun <R> create(address: String, port: Int, apply: ru.spbstu.icc.kspt.Socket.() -> R): R =
                java.net.Socket(address, port).use { Socket(it).apply() }
    }
}

fun <R> ServerSocket.acceptWrapped(apply: ru.spbstu.icc.kspt.Socket.() -> R): R =
        Socket(accept()).apply()
