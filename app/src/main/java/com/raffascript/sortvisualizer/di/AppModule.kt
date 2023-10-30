package com.raffascript.sortvisualizer.di

import android.content.Context
import com.raffascript.sortvisualizer.core.data.AlgorithmRegister
import com.raffascript.sortvisualizer.visualization.data.AlgorithmRepository
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesDataSource
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesRepository
import com.raffascript.sortvisualizer.visualization.domain.ChangeDelayUseCase
import com.raffascript.sortvisualizer.visualization.domain.ChangeListSizeUseCase
import com.raffascript.sortvisualizer.visualization.domain.FormatListSizeInputUseCase
import com.raffascript.sortvisualizer.visualization.domain.LoadUserPreferencesUseCase
import com.raffascript.sortvisualizer.visualization.domain.ValidateListSizeUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.GetAlgorithmProgressFlowUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.PauseAlgorithmUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.RestartAlgorithmUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.ResumeAlgorithmUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.SetAlgorithmUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.StartAlgorithmUseCase

interface AppModule {
    val algorithmRegister: AlgorithmRegister
    val userPreferencesRepository: UserPreferencesRepository
    val algorithmRepository: AlgorithmRepository
    val userPreferencesDataSource: UserPreferencesDataSource
    val changeListSizeUseCase: ChangeListSizeUseCase
    val formatListSizeInputUseCase: FormatListSizeInputUseCase
    val validateListSizeUseCase: ValidateListSizeUseCase
    val changeDelayUseCase: ChangeDelayUseCase
    val loadUserPreferencesUseCase: LoadUserPreferencesUseCase
    val getAlgorithmProgressFlowUseCase: GetAlgorithmProgressFlowUseCase
    val pauseAlgorithmUseCase: PauseAlgorithmUseCase
    val restartAlgorithmUseCase: RestartAlgorithmUseCase
    val resumeAlgorithmUseCase: ResumeAlgorithmUseCase
    val setAlgorithmUseCase: SetAlgorithmUseCase
    val startAlgorithmUseCase: StartAlgorithmUseCase
}

class AppModuleImpl(private val context: Context) : AppModule {
    override val algorithmRegister: AlgorithmRegister by lazy {
        AlgorithmRegister()
    }
    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(userPreferencesDataSource)
    }
    override val algorithmRepository: AlgorithmRepository by lazy {
        AlgorithmRepository(loadUserPreferencesUseCase)
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
        ChangeDelayUseCase(algorithmRepository, userPreferencesRepository)
    }
    override val loadUserPreferencesUseCase: LoadUserPreferencesUseCase by lazy {
        LoadUserPreferencesUseCase(userPreferencesRepository)
    }
    override val getAlgorithmProgressFlowUseCase: GetAlgorithmProgressFlowUseCase by lazy {
        GetAlgorithmProgressFlowUseCase(algorithmRepository)
    }
    override val pauseAlgorithmUseCase: PauseAlgorithmUseCase by lazy {
        PauseAlgorithmUseCase(algorithmRepository)
    }
    override val restartAlgorithmUseCase: RestartAlgorithmUseCase by lazy {
        RestartAlgorithmUseCase(algorithmRepository)
    }
    override val resumeAlgorithmUseCase: ResumeAlgorithmUseCase by lazy {
        ResumeAlgorithmUseCase(algorithmRepository)
    }
    override val setAlgorithmUseCase: SetAlgorithmUseCase by lazy {
        SetAlgorithmUseCase(algorithmRepository)
    }
    override val startAlgorithmUseCase: StartAlgorithmUseCase by lazy {
        StartAlgorithmUseCase(algorithmRepository)
    }
}