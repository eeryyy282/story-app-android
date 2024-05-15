package com.example.storyappjuzairi.view.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.data.pref.SettingPreference

class SettingViewModelFactory(
    private val pref: SettingPreference
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak diketahui " + modelClass.name)
    }
}