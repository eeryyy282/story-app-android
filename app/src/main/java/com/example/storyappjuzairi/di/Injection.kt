package com.example.storyappjuzairi.di

import android.content.Context
import com.example.storyappjuzairi.data.UserRepository
import com.example.storyappjuzairi.data.pref.UserPreference
import com.example.storyappjuzairi.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}