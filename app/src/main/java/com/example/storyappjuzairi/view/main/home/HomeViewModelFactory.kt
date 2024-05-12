package com.example.storyappjuzairi.view.main.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.data.pref.UserPreference
import com.example.storyappjuzairi.data.repository.StoryRepository
import com.example.storyappjuzairi.di.Injection

class HomeViewModelFactory(
    private val storyRepository: StoryRepository,
    private val userPreference: UserPreference
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository, userPreference) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak ditemukan: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null

        fun getInstance(
            context: Context
        ): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(
                    Injection.storyRepository(context),
                    Injection.userPreference(context)
                )
            }.also { instance = it }
    }

}