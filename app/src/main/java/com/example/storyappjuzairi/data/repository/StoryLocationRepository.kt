package com.example.storyappjuzairi.data.repository

import android.util.Log
import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.data.response.ListStoryItem
import com.example.storyappjuzairi.data.retrofit.ApiService

class StoryLocationRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun getStoryLocation(): Result<List<ListStoryItem?>> {
        try {
            val response = apiService.getStoriesWithLocation()
            if (response.error == true) {
                return Result.Error("Error: ${response.message}")
            } else {
                val storyLocation = response.listStory ?: emptyList()
                return Result.Success(storyLocation)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getStoryLocation: ${e.message.toString()}")
            return Result.Error(e.message.toString())
        }
    }

    companion object {
        private const val TAG = "StoryLocationRepository"

        @Volatile
        private var instance: StoryLocationRepository? = null

        fun getInstance(apiService: ApiService): StoryLocationRepository =
            instance ?: synchronized(this) {
                instance ?: StoryLocationRepository(apiService)
            }.also { instance = it }
    }
}