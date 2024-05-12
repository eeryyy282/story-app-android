package com.example.storyappjuzairi.view.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.data.pref.UserPreference
import com.example.storyappjuzairi.data.repository.StoryRepository
import com.example.storyappjuzairi.data.response.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val userPreference: UserPreference
) : ViewModel() {
    private val _story = MutableLiveData<Result<List<ListStoryItem?>>>()
    val story: LiveData<Result<List<ListStoryItem?>>>
        get() = _story

    private val _userId = MutableLiveData<String>()

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            _userId.value = userPreference.getUserId()
            _userName.value = userPreference.getUserName()
        }
    }

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