package com.raffascript.sortvisualizer.di

import android.content.Context
import com.raffascript.sortvisualizer.core.data.AlgorithmRegister
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesDataSource
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesRepository
import com.raffascript.sortvisualizer.visualization.domain.ChangeDelayUseCase
import com.raffascript.sortvisualizer.visualization.domain.ChangeListSizeUseCase
import com.raffascript.sortvisualizer.visualization.domain.FormatListSizeInputUseCase
import com.raffascript.sortvisualizer.visualization.domain.LoadUserPreferencesUseCase
import com.raffascript.sortvisualizer.visualization.domain.ValidateListSizeUseCase

interface AppModule {
    val algorithmRegister: AlgorithmRegister
    val userPreferencesRepository: UserPreferencesRepository
    val userPreferencesDataSource: UserPreferencesDataSource
    val changeListSizeUseCase: ChangeListSizeUseCase
    val formatListSizeInputUseCase: FormatListSizeInputUseCase
    val validateListSizeUseCase: ValidateListSizeUseCase
    val changeDelayUseCase: ChangeDelayUseCase
    val loadUserPreferencesUseCase: LoadUserPreferencesUseCase
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
    override val changeListSizeUseCase: ChangeListSizeUseCase by lazy {
        ChangeListSizeUseCase(validateListSizeUseCase, formatListSizeInputUseCase, userPreferencesRepository)
    }
    override val formatListSizeInputUseCase: FormatListSizeInputUseCase by lazy {
        FormatListSizeInputUseCase()
    }
    override val validateListSizeUseCase: ValidateListSizeUseCase by lazy {
        ValidateListSizeUseCase()
    }
    override val changeDelayUseCase: ChangeDelayUseCase by lazy {
        ChangeDelayUseCase(userPreferencesRepository)
    }
    override val loadUserPreferencesUseCase: LoadUserPreferencesUseCase by lazy {
        LoadUserPreferencesUseCase(userPreferencesRepository)
    }
}