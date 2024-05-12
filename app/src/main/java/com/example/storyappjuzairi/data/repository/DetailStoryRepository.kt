package com.example.storyappjuzairi.data.repository

import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.data.response.Story
import com.example.storyappjuzairi.data.retrofit.ApiService

class DetailStoryRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun getDetailStory(storyId: String): Result<Story?> {
        try {
            val response = apiService.getDetailStories(storyId)
            if (response.error == true) {
                return Result.Error("Error: ${response.message}")
            } else {
                val storyDetail = response.story
                return Result.Success(storyDetail)
            }
        } catch (e: Exception) {
            return Result.Error(e.message.toString())
        }
    }

    companion object {

        @Volatile
        private var instance: DetailStoryRepository? = null

        fun getInstance(
            apiService: ApiService
        ): DetailStoryRepository =
            instance ?: synchronized(this) {
                instance ?: DetailStoryRepository(apiService)
            }.also { instance = it }
    }


}