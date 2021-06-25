package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import model.RemoteModel
import model.RemoteModelImpl
import view.Joystick.OnJoystickMovedListener
import view.databinding.ActivityMainBinding
import viewmodel.RemoteViewModel
import viewmodel.RemoteViewModelImpl


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : RemoteViewModel

    companion object {
        private const val SEEKBAR_MAX = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model : RemoteModel = RemoteModelImpl()
        viewModel = RemoteViewModelImpl(model, SEEKBAR_MAX)


        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        binding.rudder.max = SEEKBAR_MAX
        binding.throttle.max = SEEKBAR_MAX

        // bind joystick move to aileron and elevator
        binding.joystick.setOnJoystickMovedListener(object : OnJoystickMovedListener {
            override fun onJoystickMoved(x: Float, y: Float) {
                viewModel.elevator = x.toDouble()

                // since negative y value is up, we will take the negative of y
                viewModel.aileron = -y.toDouble()
            }
        })
    }
}