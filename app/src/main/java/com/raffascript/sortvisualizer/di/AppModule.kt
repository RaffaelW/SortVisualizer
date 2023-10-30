package com.raffascript.sortvisualizer.di

import android.content.Context
import com.raffascript.sortvisualizer.core.data.AlgorithmRegister
import com.raffascript.sortvisualizer.visualization.data.AlgorithmRepository
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesDataSource
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesRepository

interface AppModule {
    val algorithmRegister: AlgorithmRegister
    val userPreferencesRepository: UserPreferencesRepository
    val algorithmRepository: AlgorithmRepository
    val userPreferencesDataSource: UserPreferencesDataSource
}

class AppModuleImpl(private val context: Context) : AppModule {
    override val algorithmRegister: AlgorithmRegister by lazy {
        AlgorithmRegister()
    }
    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(userPreferencesDataSource)
    }
    override val algorithmRepository: AlgorithmRepository by lazy {
        AlgorithmRepository(userPreferencesRepository)
    }
    override val userPreferencesDataSource: UserPreferencesDataSource by lazy {
        UserPreferencesDataSource(context)
    }
}