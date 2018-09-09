package ru.spbstu.icc.kspt

import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.coroutines.experimental.buildSequence
import java.net.ServerSocket
import java.net.SocketException


class Server {
    private var serverStopRequests = false
    private val dataHandler = DataHandler()
    private val idleConnections = ArrayList<Connection>()
    private val pool = Executors.newFixedThreadPool(Configuration.SERVER_THREADS)
    private val logger = Logger()
    private val idGenerator = IdGenerator()
    private val acceptThread: Thread
    private val handleThread: Thread

    private fun createHandler(id: String, socket: SocketWrapper) = buildSequence {
        var close = false
        while (!close) {
            val command = Command.valueOf(socket.readln())
            when (command) {
                Command.WRITE -> {
                    yield(false)
                    dataHandler.writeRaw(socket.readln())
                }
                Command.READ -> {
                    socket.writeln(dataHandler.readStatus())
                }
                Command.CLOSE -> {
                    close = true
                }
                Command.SHUTDOWN -> {
                    close = true
                    serverStopRequests = true
                }
            }
            logger.info("Command $command is done")
            yield(close)
        }
        logger.info("Shutdown connection $id")
    }

    private fun ready() = synchronized(idleConnections) {
        idleConnections.find { it.socket.ready() }
    }

    private fun register(socket: SocketWrapper) = synchronized(idleConnections) {
        val id = idGenerator.next()
        val handler = createHandler(id, socket).iterator()
        val connection = Connection(id, socket, handler)
        idleConnections.add(connection)
        logger.info("Register connection ${connection.id}")
    }

    private fun launch(connection: Connection) = synchronized(idleConnections) {
        idleConnections.remove(connection)
        pool.execute {
            val close = connection.handler.next()
            if (close) {
                // Finalize coroutine thread
                connection.handler.forEach { }
                connection.socket.close()
            } else {
                synchronized(idleConnections) {
                    idleConnections.add(connection)
                }
            }
        }
        logger.info("Launch connection ${connection.id}")
    }

    fun join() {
        acceptThread.join()
        handleThread.join()
        pool.shutdown()
    }

    fun start() {
        acceptThread.start()
        handleThread.start()
    }

    data class Connection(val id: String, val socket: SocketWrapper, val handler: Iterator<Boolean>)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val server = Server()
            server.start()
            server.join()
        }
    }

    init {
        val serverSocket = ServerSocket(Configuration.SERVER_PORT)
        acceptThread = thread(start = false) {
            logger.info("Start accept thread")
            serverSocket.use { listener ->
                try {
                    while (!serverStopRequests) {
                        val socket = listener.accept()
                        val wrapper = SocketWrapper(socket)
                        register(wrapper)
                    }
                } catch (exception: SocketException) {
                    logger.warn(exception)
                }
            }
            logger.info("Shutdown accept thread")
        }
        handleThread = thread(start = false) {
            logger.info("Start handle thread")
            while (!serverStopRequests) {
                val connection = ready()
                when (connection) {
                    null -> Thread.yield()
                    else -> launch(connection)
                }
            }
            logger.info("Shutdown handle thread")
            serverSocket.close()
        }
    }
}