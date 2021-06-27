package model

import androidx.databinding.BaseObservable
import java.io.PrintWriter
import java.net.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

/**
 * the implementation of the remote controller model.
 * it is an active object -
 *      meaning all tasks run on a different thread on the background.
 */
class RemoteModelImpl : BaseObservable(), RemoteModel{

    // the timeout while creating socket
    companion object{
        private const val TIMEOUT : Int = 500
    }

    private lateinit var server : Socket            // the connection to the server.
    private val tasks : BlockingQueue<Runnable>     // the tasks queue.
    private var stop : Boolean = false              // whether it should stop.
    override var isConnected : Boolean = false               // whether the connection is alive
        set(value) {
            // notify the value is changed
            if (value != field) {

                field = value
                notifyChange()
            }
        }


    init {
        // creating the tasks queue.
        tasks = LinkedBlockingQueue()

        // running the background thread, which handles the tasks.
        Thread(
            { run() }, "cursed thread"
        ).start()

    }

    /**
     * this method is responsible for running the tasks.
     * handles them one by one.
     */
    private fun run() {
        while (!stop) {
            // blocking method - waits for a tasks (Runnable).
            val toRun : Runnable = tasks.take()

            try {
                toRun.run()
            } catch (e : Exception) {
            }
        }
    }

    /**
     * creates a new connection.
     * does that on the same thread.
     * @param ip the ip of the server.
     * @param port the port of the server.
     */
    private fun innerConnect(ip : String, port : Int) {
        // trying to close the old socket.
        if (isConnected) {
            try {
                server.close()
            } catch (e: Exception) {
            }
        }

        try {
            server = Socket()
            server.connect(InetSocketAddress(ip, port), TIMEOUT)
            server.soTimeout = TIMEOUT
            isConnected = true
        } catch (e : Exception) {
            isConnected = false
        }

    }

    /**
     * creating a new task of sending a string to the server.
     * @param toSend the string to send.
     */
    private fun sendStringTask(toSend : String) {
        val toRun = Runnable {
            try {
                val out = PrintWriter(server.getOutputStream(),true)
                out.print(toSend + "\r\n")
                out.flush()
            } catch (e : Exception) {
                isConnected = false
            }
        }
        tasks.add(toRun)
    }

    /**
     * this method adds a new task of connecting to the server.
     * @param ip the ip of the server.
     * @param port the port of the server.
     */
    override fun connect(ip : String, port : Int) {
        val toRun = Runnable { innerConnect(ip, port) }
        tasks.add(toRun)
    }

    /**
     * the four properties of the remote controller.
     * all setters will also send a message to the server,
     * informing the new value of the variable.
     */
    override var aileron : Double = 0.0
        set(value) {
            sendStringTask("set /controls/flight/aileron $value")
            field = value
        }

    override var elevator : Double = 0.0
        set(value) {
            sendStringTask("set /controls/flight/elevator $value")
            field = value
        }

    override var rudder : Double = 0.0
        set(value) {
            sendStringTask("set /controls/flight/rudder $value")
            field = value
        }

    override var throttle : Double = 0.0
        set(value) {
            sendStringTask("set /controls/engines/current-engine/throttle $value")
            field = value
        }

    override fun close() {
        val toRun1 = { server.close() }
        val toRun2 = { stop = true }

        tasks.add(toRun1)
        tasks.add(toRun2)
    }
}