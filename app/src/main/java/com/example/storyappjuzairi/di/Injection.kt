package com.example.storyappjuzairi.di


import android.content.Context
import com.example.storyappjuzairi.data.pref.UserPreference
import com.example.storyappjuzairi.data.pref.dataStore
import com.example.storyappjuzairi.data.repository.LoginRepository
import com.example.storyappjuzairi.data.repository.RegisterRepository
import com.example.storyappjuzairi.data.repository.StoryRepository
import com.example.storyappjuzairi.data.retrofit.ApiConfig
import kotlinx.coroutines.runBlocking


object Injection {

    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return StoryRepository.getInstance(apiService, pref)
    }

    fun registerRepository(context: Context): RegisterRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return RegisterRepository.getInstance(apiService)
    }

    fun loginRepository(context: Context): LoginRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return LoginRepository.getInstance(apiService)
    }
}