package com.example.storyappjuzairi.view.main.add_story

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.R
import com.example.storyappjuzairi.data.repository.AddNewStoryRepository
import com.example.storyappjuzairi.di.Injection

class AddStoryViewModelFactory(
    private val addStoryRepository: AddNewStoryRepository
) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(addStoryRepository) as T
        } else {
            throw IllegalArgumentException("viewmodel class tidak ditemukan" + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: AddStoryViewModelFactory? = null

        fun getInstance(context: Context): AddStoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: AddStoryViewModelFactory(
                    Injection.addStoryRepository(context)
                )
            }.also { instance = it }
    }
}