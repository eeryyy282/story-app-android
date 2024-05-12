package com.example.storyappjuzairi.view.main.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.data.repository.StoryRepository
import com.example.storyappjuzairi.data.response.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    private val _story = MutableLiveData<Result<List<ListStoryItem?>>>()
    val story: LiveData<Result<List<ListStoryItem?>>>
        get() = _story

    fun findStory() {
        viewModelScope.launch {
            try {

                val result = storyRepository.getStory()
                _story.value = result
            } catch (e: Exception) {
                _story.value = Result.Error(e.message.toString())
                Log.e(TAG, "Error: ${e.message.toString()}")
            }
        }

    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}