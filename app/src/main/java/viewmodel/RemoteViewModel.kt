package viewmodel

import android.graphics.Color
import androidx.databinding.Bindable
import androidx.databinding.Observable

interface RemoteViewModel : Observable {
    fun connect()

    fun close()

    var ip : String
    var port : String

    var aileron : Double
    var elevator : Double
    var rudder : Int
    var throttle : Int
    // connected button should change color when connection is alive

    val connectedButtonColor : Int
}