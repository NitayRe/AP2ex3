package viewmodel

interface RemoteViewModel {
    fun connect()

    var ip : String
    var port : Int

    var aileron : Double
    var elevator : Double
    var rudder : Double
    var throttle : Double
}