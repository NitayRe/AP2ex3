package viewmodel

interface RemoteViewModel {
    fun connect()

    var ip : String
    var port : String

    var aileron : Double
    var elevator : Double
    var rudder : Int
    var throttle : Int
}