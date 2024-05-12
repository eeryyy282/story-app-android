package com.example.storyappjuzairi.data.repository

import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.data.pref.UserPreference
import com.example.storyappjuzairi.data.response.ListStoryItem
import com.example.storyappjuzairi.data.retrofit.ApiService

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    private suspend fun getToken(): String {
        val token = userPreference.getUserToken()
        return token
    }

    suspend fun getStory(): Result<List<ListStoryItem?>> {
        try {

            val response = apiService.getStories()
            if (response.error == true) {
                return Result.Error("Error: ${response.message}")
            } else {
                val story = response.listStory ?: emptyList()
                return Result.Success(story)
            }
        } catch (e: Exception) {
            return Result.Error(e.toString())
        }
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
