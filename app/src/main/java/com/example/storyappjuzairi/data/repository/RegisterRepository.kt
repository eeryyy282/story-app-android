package com.example.storyappjuzairi.data.repository

import com.example.storyappjuzairi.data.response.RegisterResponse
import com.example.storyappjuzairi.data.retrofit.ApiService

class RegisterRepository(
    private val apiService: ApiService
) {

    suspend fun registerUser(username: String, email: String, password: String): RegisterResponse {
        return apiService.registerUser(username, email, password)
    }

    companion object {

        @Volatile
        private var instance: RegisterRepository? = null

        fun getInstance(
            apiService: ApiService,
        ): RegisterRepository =
            instance ?: synchronized(this) {
                instance ?: RegisterRepository(apiService)
            }.also { instance = it }
    }
}