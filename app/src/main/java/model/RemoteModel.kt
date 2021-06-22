package model


interface RemoteModel {


    fun connect(ip : String, port : Int)

    var aileron : Double
    var elevator : Double
    var rudder : Double
    var throttle : Double
}