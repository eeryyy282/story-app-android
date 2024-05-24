package com.example.storyappjuzairi.view.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.data.repository.StoryLocationRepository
import com.example.storyappjuzairi.di.Injection

class MapsViewModelFactory private constructor(
    private val storyLocationRepository: StoryLocationRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyLocationRepository) as T
        } else {
            throw IllegalArgumentException("Viewmodel class tidak diketahui: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: MapsViewModelFactory? = null
        fun getInstance(
            context: Context
        ): MapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MapsViewModelFactory(
                    Injection.storyLocationRepository(context)
                )
            }.also { instance = it }
    }
}