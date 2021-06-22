package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import androidx.databinding.DataBindingUtil
import model.RemoteModel
import model.RemoteModelImpl
import view.databinding.ActivityMainBinding
import viewmodel.RemoteViewModel
import viewmodel.RemoteViewModelImpl


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : RemoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var model : RemoteModel = RemoteModelImpl()
        viewModel = RemoteViewModelImpl(model)


        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
    }
}