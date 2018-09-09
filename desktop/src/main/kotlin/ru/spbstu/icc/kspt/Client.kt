package ru.spbstu.icc.kspt

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

import javax.swing.JOptionPane

/**
 * Trivial client for the date server.
 */
object Client {

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val s = Socket(Configuration.SERVER_IP, Configuration.SERVER_PORT)
        val input = BufferedReader(InputStreamReader(s.getInputStream()))
        val answer = input.readLine()
        JOptionPane.showMessageDialog(null, answer)
        System.exit(0)
    }
}