package com.raffascript.sortvisualizer.di

import com.raffascript.sortvisualizer.visualization.domain.ChangeDelayUseCase
import com.raffascript.sortvisualizer.visualization.domain.ChangeListSizeUseCase
import com.raffascript.sortvisualizer.visualization.domain.FormatListSizeInputUseCase
import com.raffascript.sortvisualizer.visualization.domain.LoadUserPreferencesUseCase
import com.raffascript.sortvisualizer.visualization.domain.ValidateListSizeUseCase

interface UseCaseModule {
    val formatListSizeInputUseCase: FormatListSizeInputUseCase
    val validateListSizeUseCase: ValidateListSizeUseCase

    val loadUserPreferencesUseCase: LoadUserPreferencesUseCase
    val changeListSizeUseCase: ChangeListSizeUseCase
    val changeDelayUseCase: ChangeDelayUseCase
}

class UseCaseModuleImpl(private val appModule: AppModule) : UseCaseModule {
    override val formatListSizeInputUseCase: FormatListSizeInputUseCase by lazy {
        FormatListSizeInputUseCase()
    }
    override val validateListSizeUseCase: ValidateListSizeUseCase by lazy {
        ValidateListSizeUseCase()
    }

    override val loadUserPreferencesUseCase: LoadUserPreferencesUseCase by lazy {
        LoadUserPreferencesUseCase(appModule.userPreferencesRepository)
    }
    override val changeListSizeUseCase: ChangeListSizeUseCase by lazy {
        ChangeListSizeUseCase(validateListSizeUseCase, formatListSizeInputUseCase, appModule.userPreferencesRepository)
    }
    override val changeDelayUseCase: ChangeDelayUseCase by lazy {
        ChangeDelayUseCase(appModule.userPreferencesRepository)
    }
}