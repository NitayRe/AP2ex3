package viewmodel

import android.graphics.Color
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.library.baseAdapters.BR
import model.RemoteModel

class RemoteViewModelImpl(private var model : RemoteModel, private val seekbarMax : Int) : BaseObservable(), RemoteViewModel {

    init {
        // add listener to model
        model.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                Log.d("changed", "changed")
                connectedButtonColor = if (model.isConnected) {
                    Color.GREEN
                } else {
                    Color.RED
                }
                notifyChange()
            }
        })
    }

    override fun connect() {
        if (this.port.toIntOrNull() != null) {
            model.connect(this.ip, this.port.toInt())
        }
    }


    override var ip : String = ""
    override var port : String = ""

    @Bindable
    override var connectedButtonColor = Color.RED

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
            // normalize values to range of -1 to 1
            model.rudder = (value.toDouble() - 0.5 * seekbarMax) / (0.5 * seekbarMax)
            field = value
        }
    override var throttle : Int = 0
        set(value) {
            // normalize values to range of 0 to 1
            model.throttle = value.toDouble() / seekbarMax
            field = value
        }
}