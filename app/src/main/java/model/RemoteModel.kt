package model


interface RemoteModel {


    fun connect(ip : String, port : Int)

    fun close()

    var aileron : Double
    var elevator : Double
    var rudder : Double
    var throttle : Double
}