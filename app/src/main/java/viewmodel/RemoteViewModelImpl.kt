package viewmodel

import androidx.core.text.isDigitsOnly
import model.RemoteModel

class RemoteViewModelImpl(private var model : RemoteModel, private val seekbarMax : Int) : RemoteViewModel {

    override fun connect() {
        if (this.port.toIntOrNull() != null) {
            model.connect(this.ip, this.port.toInt())
        }
    }

    override var ip : String = ""
    override var port : String = ""

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