package ru.spbstu.icc.kspt

import java.io.File
import java.net.URI

object Configuration {

    private val CONFIG_PATTERN = "([^\\s]+)\\s*=\\s*([^\\s].*)".toRegex()

    private val CONFIGURATION = javaClass.getResource("/configuration.cfg")
            .toURI()
            .toFile()
            .readLines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filterNot { it.startsWith("//") }
            .map { CONFIG_PATTERN.matchEntire(it) ?: throw IllegalFormatException(it) }
            .map { it.destructured }
            .map { it.component1() to it.component2() }
            .toMap()

    val FREQUENCY = configuration("frequency") { toInt() }

    val SERVER_IP = configuration("server-ip")

    val SERVER_PORT = configuration("server-port") { toInt() }

    private fun <T> configuration(name: String, cast: String.() -> T): T = configuration(name).cast()

    private fun configuration(name: String) = CONFIGURATION[name] ?: throw ConfigurationNotFoundException(name)

    private fun URI.toFile() = File(this)

    class IllegalFormatException(line: String) : Exception("Illegal format of configuration line $line")

    class ConfigurationNotFoundException(name: String) : Exception("Configuration '$name' not found")
}