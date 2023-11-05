package com.raffascript.sortvisualizer.visualization.domain

import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesRepository

class ChangeSoundEnabledUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(isSoundEnabled: Boolean) {
        userPreferencesRepository.saveSoundEnabled(isSoundEnabled)
    }
}