package model

import java.net.Socket


class RemoteModelImpl : RemoteModel{
    private lateinit var socket : Socket

    override fun connect(ip : String, port : Int) {
        if (this::socket.isInitialized) {
            socket.close()
        }

        socket = Socket(ip, port)
        socket.outputStream.write("Hello from the client!".toByteArray())
    }

    override var aileron : Double = 0.0
    override var elevator : Double = 0.0
    override var rudder : Double = 0.0
    override var throttle : Double = 0.0
}