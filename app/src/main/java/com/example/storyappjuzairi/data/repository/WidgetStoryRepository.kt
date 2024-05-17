package com.example.storyappjuzairi.data.repository

import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.data.response.ListStoryItem
import com.example.storyappjuzairi.data.retrofit.ApiService

class WidgetStoryRepository private constructor(
    private val apiService: ApiService
) {
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
            return Result.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var instance: WidgetStoryRepository? = null

        fun getInstance(apiService: ApiService): WidgetStoryRepository =
            instance ?: synchronized(this) {
                instance ?: WidgetStoryRepository(apiService)
            }.also { instance = it }
    }
}