package viewmodel

import model.RemoteModel

class RemoteViewModelImpl(private var model : RemoteModel, private val seekbarMax : Int) : RemoteViewModel {
    override fun connect() {
        model.connect(this.ip, this.port)
    }

    override var ip : String = "127.0.0.1"
    override var port : Int = 9876

    override var aileron : Double = 0.0
        set(value) {
            model.aileron = value
            field = value
        }
    override var elevator : Double = 0.0
        set(value) {
            model.elevator = value
            field = value
        }
    override var rudder : Int = seekbarMax / 2
        set(value) {
            model.rudder = (value.toDouble() - 0.5 * seekbarMax) / (0.5 * seekbarMax)
            field = value
        }
    override var throttle : Int = 0
        set(value) {
            model.throttle = value.toDouble() / seekbarMax
            field = value
        }
}