package com.raffascript.sortvisualizer.visualization.domain

import com.raffascript.sortvisualizer.core.util.Resource
import com.raffascript.sortvisualizer.visualization.data.AlgorithmRepository
import com.raffascript.sortvisualizer.visualization.data.DelayValue
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesRepository

class ChangeDelayUseCase(
    private val algorithmRepository: AlgorithmRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {

    suspend operator fun invoke(delay: DelayValue): Resource<Unit> {
        algorithmRepository.changeDelay(delay.millis)
        userPreferencesRepository.saveDelay(delay)
        return Resource.Success(Unit)
    }
}