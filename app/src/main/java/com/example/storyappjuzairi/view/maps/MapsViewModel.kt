package com.example.storyappjuzairi.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.data.repository.StoryLocationRepository
import com.example.storyappjuzairi.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(
    private val storyLocationRepository: StoryLocationRepository
) : ViewModel() {
    private val _location = MutableLiveData<Result<List<ListStoryItem?>>>()
    val location: LiveData<Result<List<ListStoryItem?>>>
        get() = _location

    fun getStoryLocation() {
        viewModelScope.launch {
            try {
                val result = storyLocationRepository.getStoryLocation()
                _location.value = result
                Log.d(TAG, "getStoryLocation: $result")
            } catch (e: Exception) {
                _location.value = Result.Error(e.message.toString())
                Log.e(TAG, "Error: &{e.message}")
            }

        }
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }

}