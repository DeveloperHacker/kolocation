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

    val SERVER_IP = configuration("server-ip")
    val SERVER_PORT = configuration("server-port").toInt()
    val SERVER_THREADS = configuration("server-threads").toInt()

    val GENERATION_FREQUENCY = configuration("generation-frequency").toInt()
    val GENERATION_STEPS = configuration("generation-steps").toInt()

    private fun configuration(name: String) = CONFIGURATION[name] ?: throw ConfigurationNotFoundException(name)

    private fun URI.toFile() = File(this)

    class IllegalFormatException(line: String) : Exception("Illegal format of configuration line $line")

    class ConfigurationNotFoundException(name: String) : Exception("Configuration '$name' not found")
}