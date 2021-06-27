package model

import androidx.databinding.BaseObservable
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

/**
 * the implementation of the remote controller model.
 * it is an active object -
 *      meaning all tasks run on a different thread on the background.
 */
class RemoteModelImpl : BaseObservable(), RemoteModel{
    private lateinit var socket : Socket            // the connection to the server.
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
        Thread{
            run()
        }.start()

    }

    /**
     * this method is responsible for running the tasks.
     * handles them one by one.
     */
    private fun run() {
        while (!stop) {
            val toRun = tasks.take()    // blocking method - waits for a tasks.
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
        if (this::socket.isInitialized) {
            socket.close()
            isConnected = false
        }
        try {
            socket = Socket(ip, port)
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
        val toRun = {
            try {
                val out = PrintWriter(socket.getOutputStream(),true)
                out.print(toSend + "\r\n")
                out.flush()
            } catch (e : IOException) {
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
        val toRun = Runnable { innerConnect(ip, port)}
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
        val toRun1 = { socket.close() }
        val toRun2 = { stop = true }

        tasks.add(toRun1)
        tasks.add(toRun2)
    }
}