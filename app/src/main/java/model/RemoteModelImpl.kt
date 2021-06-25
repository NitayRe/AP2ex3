package model

import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue


class RemoteModelImpl : RemoteModel{
    private lateinit var socket : Socket
    private val tasks : BlockingQueue<Runnable>
    private var stop : Boolean = false
    init {
        // creating the tasks queue.
        tasks = LinkedBlockingQueue()

        Thread{
            run()
        }.start()

    }


    private fun run() {
        while (!stop) {
            val toRun = tasks.take()
            try {
                toRun.run()
            } catch (e : Exception) {
            }
        }
    }

    private fun innerConnect(ip : String, port : Int) {
        if (this::socket.isInitialized) {
            socket.close()
        }

        socket = Socket(ip, port)
    }

    private fun sendStringTask(toSend : String) {
        val toRun = {
            val out = PrintWriter(socket.getOutputStream(),true)
            out.print(toSend + "\r\n")
            out.flush()
        }
        tasks.add(toRun)
    }

    override fun connect(ip : String, port : Int) {
        val toRun = Runnable { innerConnect(ip, port)}
        tasks.add(toRun)
        sendStringTask(("creating connection"))
    }

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