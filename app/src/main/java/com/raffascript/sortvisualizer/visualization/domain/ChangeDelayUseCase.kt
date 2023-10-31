package com.raffascript.sortvisualizer.visualization.domain

import com.raffascript.sortvisualizer.core.util.Resource
import com.raffascript.sortvisualizer.visualization.data.DelayValue
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesRepository

class ChangeDelayUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    suspend operator fun invoke(delay: DelayValue): Resource<Unit> {
        userPreferencesRepository.saveDelay(delay)
        return Resource.Success(Unit)
    }
}