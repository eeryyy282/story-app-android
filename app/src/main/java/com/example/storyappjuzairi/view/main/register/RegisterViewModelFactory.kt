package com.example.storyappjuzairi.view.main.register

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.data.repository.RegisterRepository
import com.example.storyappjuzairi.di.Injection

class RegisterViewModelFactory private constructor(
    private val application: Application,
    private val registerRepository: RegisterRepository
) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(application, registerRepository) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak ditemukan")
    }

    companion object {
        @Volatile
        private var instance: RegisterViewModelFactory? = null

        fun getInstance(application: Application): RegisterViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RegisterViewModelFactory(
                    application,
                    Injection.registerRepository(application)
                )
            }.also { instance = it }
    }

}