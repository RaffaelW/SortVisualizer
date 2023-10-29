package com.raffascript.sortvisualizer.visualization.domain

import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferences
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class LoadUserPreferencesUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    operator fun invoke(): Flow<UserPreferences> {
        return userPreferencesRepository.getUserPreferencesFlow()
    }
}