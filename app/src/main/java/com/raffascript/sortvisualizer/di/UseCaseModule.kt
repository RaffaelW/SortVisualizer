package com.raffascript.sortvisualizer.di

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

interface UseCaseModule {
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

class UseCaseModuleImpl(private val appModule: AppModule) : UseCaseModule {
    override val changeListSizeUseCase: ChangeListSizeUseCase by lazy {
        ChangeListSizeUseCase(validateListSizeUseCase, formatListSizeInputUseCase, appModule.userPreferencesRepository)
    }
    override val formatListSizeInputUseCase: FormatListSizeInputUseCase by lazy {
        FormatListSizeInputUseCase()
    }
    override val validateListSizeUseCase: ValidateListSizeUseCase by lazy {
        ValidateListSizeUseCase()
    }
    override val changeDelayUseCase: ChangeDelayUseCase by lazy {
        ChangeDelayUseCase(appModule.algorithmRepository, appModule.userPreferencesRepository)
    }
    override val loadUserPreferencesUseCase: LoadUserPreferencesUseCase by lazy {
        LoadUserPreferencesUseCase(appModule.userPreferencesRepository)
    }
    override val getAlgorithmProgressFlowUseCase: GetAlgorithmProgressFlowUseCase by lazy {
        GetAlgorithmProgressFlowUseCase(appModule.algorithmRepository)
    }
    override val pauseAlgorithmUseCase: PauseAlgorithmUseCase by lazy {
        PauseAlgorithmUseCase(appModule.algorithmRepository)
    }
    override val restartAlgorithmUseCase: RestartAlgorithmUseCase by lazy {
        RestartAlgorithmUseCase(appModule.algorithmRepository)
    }
    override val resumeAlgorithmUseCase: ResumeAlgorithmUseCase by lazy {
        ResumeAlgorithmUseCase(appModule.algorithmRepository)
    }
    override val setAlgorithmUseCase: SetAlgorithmUseCase by lazy {
        SetAlgorithmUseCase(appModule.algorithmRepository)
    }
    override val startAlgorithmUseCase: StartAlgorithmUseCase by lazy {
        StartAlgorithmUseCase(appModule.algorithmRepository)
    }
}