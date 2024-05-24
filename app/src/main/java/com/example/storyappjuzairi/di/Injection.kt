package com.example.storyappjuzairi.di


import android.content.Context
import com.example.storyappjuzairi.data.pref.UserPreference
import com.example.storyappjuzairi.data.pref.dataStore
import com.example.storyappjuzairi.data.repository.AddNewStoryRepository
import com.example.storyappjuzairi.data.repository.DetailStoryRepository
import com.example.storyappjuzairi.data.repository.LoginRepository
import com.example.storyappjuzairi.data.repository.RegisterRepository
import com.example.storyappjuzairi.data.repository.StoryLocationRepository
import com.example.storyappjuzairi.data.repository.StoryRepository
import com.example.storyappjuzairi.data.repository.WidgetStoryRepository
import com.example.storyappjuzairi.data.retrofit.ApiConfig
import kotlinx.coroutines.runBlocking


object Injection {

    fun storyRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return StoryRepository.getInstance(apiService)
    }

    fun storyLocationRepository(context: Context): StoryLocationRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return StoryLocationRepository.getInstance(apiService)
    }

    fun storyDetailRepository(context: Context): DetailStoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return DetailStoryRepository.getInstance(apiService)
    }

    fun widgetStoryRepository(context: Context): WidgetStoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return WidgetStoryRepository.getInstance(apiService)
    }


    fun addStoryRepository(context: Context): AddNewStoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return AddNewStoryRepository.getInstance(apiService)
    }

    fun userPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
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