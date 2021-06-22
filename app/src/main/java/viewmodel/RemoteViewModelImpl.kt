package viewmodel

import model.RemoteModel

class RemoteViewModelImpl(private var model : RemoteModel) : RemoteViewModel {
    override fun connect() {
        model.connect(this.ip, this.port)
    }

    override var ip : String = "127.0.0.1"
    override var port : Int = 9876

    override var aileron : Double = 0.0
    override var elevator : Double = 0.0
    override var rudder : Double = 0.0
    override var throttle : Double = 0.0
}