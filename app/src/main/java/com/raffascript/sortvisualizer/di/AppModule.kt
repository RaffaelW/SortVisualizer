package com.raffascript.sortvisualizer.di

import android.content.Context
import com.raffascript.sortvisualizer.data.algorithm_data.AlgorithmRegister
import com.raffascript.sortvisualizer.data.preferences.UserPreferencesDataSource
import com.raffascript.sortvisualizer.data.preferences.UserPreferencesRepository

interface AppModule {
    val algorithmRegister: AlgorithmRegister
    val userPreferencesRepository: UserPreferencesRepository
    val userPreferencesDataSource: UserPreferencesDataSource
}

class AppModuleImpl(private val context: Context) : AppModule {
    override val algorithmRegister: AlgorithmRegister by lazy {
        AlgorithmRegister()
    }
    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(userPreferencesDataSource)
    }
    override val userPreferencesDataSource: UserPreferencesDataSource by lazy {
        UserPreferencesDataSource(context)
    }
}