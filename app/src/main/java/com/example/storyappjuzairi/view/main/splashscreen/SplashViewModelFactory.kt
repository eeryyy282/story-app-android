package com.example.storyappjuzairi.view.main.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.data.pref.UserPreference

class SplashViewModelFactory(private val userPreference: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(userPreference) as T
        } else {
            throw IllegalArgumentException("ViewModel tidak ditemukan " + modelClass.name)
        }
    }

}