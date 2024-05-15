package com.example.storyappjuzairi.view.main.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.data.pref.UserPreference
import com.example.storyappjuzairi.di.Injection

class ProfileViewModelFactory(
    private val userPreference: UserPreference
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userPreference) as T
        }
        throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: ProfileViewModelFactory? = null

        fun getInstance(context: Context)
                : ProfileViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ProfileViewModelFactory(
                    Injection.userPreference(context)
                )
            }.also { instance = it }
    }
}