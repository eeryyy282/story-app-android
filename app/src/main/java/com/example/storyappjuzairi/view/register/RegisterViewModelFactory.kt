package com.example.storyappjuzairi.view.register

import RegisterViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.di.Injection

class RegisterViewModelFactory private constructor(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(application, Injection.registerRepository()) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak ditemukan")
    }

    companion object {
        @Volatile
        private var instance: RegisterViewModelFactory? = null

        fun getInstance(): RegisterViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RegisterViewModelFactory(application = Application())
            }.also { instance = it }
    }

}