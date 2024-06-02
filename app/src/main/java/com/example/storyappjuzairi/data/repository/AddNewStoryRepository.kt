package com.example.storyappjuzairi.data.repository

import com.example.storyappjuzairi.data.response.AddNewStoryResponse
import com.example.storyappjuzairi.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddNewStoryRepository(private val apiService: ApiService) {
    suspend fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddNewStoryResponse {
        return apiService.addStories(file, description, lat, lon)
    }


    companion object {
        @Volatile
        private var instance: AddNewStoryRepository? = null

        fun getInstance(
            apiService: ApiService
        ): AddNewStoryRepository =
            instance ?: synchronized(this) {
                instance ?: AddNewStoryRepository(apiService)
            }.also { instance = it }
    }
}