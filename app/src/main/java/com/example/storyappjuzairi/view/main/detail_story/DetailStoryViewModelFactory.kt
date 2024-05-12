package com.example.storyappjuzairi.view.main.detail_story

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.data.repository.DetailStoryRepository
import com.example.storyappjuzairi.di.Injection

class DetailStoryViewModelFactory(
    private val detailStoryRepository: DetailStoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(detailStoryRepository) as T
        }
        throw IllegalArgumentException("Viewmodel class tidak ditemukan: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailStoryViewModelFactory? = null

        fun getInstance(
            context: Context
        ): DetailStoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailStoryViewModelFactory(
                    Injection.storyDetailRepository(context)
                )
            }.also { instance = it }
    }


}