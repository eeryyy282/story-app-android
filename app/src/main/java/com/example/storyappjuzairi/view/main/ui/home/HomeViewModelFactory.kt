package com.example.storyappjuzairi.view.main.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.data.repository.StoryRepository
import com.example.storyappjuzairi.di.Injection

class HomeViewModelFactory(
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository) as T
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
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }

}