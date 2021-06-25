package model

/**
 *  this is the interface of the remote model.
 *  model's implementations should be as 'active object'.
 */
interface RemoteModel {

    /**
     * connecting the remote to a new application.
     * @param ip the ip of the new connection.
     * @param port the port.
     */
    fun connect(ip : String, port : Int)

    /**
     *  closing the model object (as it should be active object).
     *  also closes all open resources - such as sockets.
     */
    fun close()

    /**
     * the four properties of the remote.
     */
    var aileron : Double
    var elevator : Double
    var rudder : Double
    var throttle : Double
}