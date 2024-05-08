package com.example.storyappjuzairi.di


import com.example.storyappjuzairi.data.repository.RegisterRepository
import com.example.storyappjuzairi.data.retrofit.ApiConfig


object Injection {
    fun registerRepository(): RegisterRepository {
        val apiService = ApiConfig.getApiService()
        return RegisterRepository.getInstance(apiService)
    }
}