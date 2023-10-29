package com.raffascript.sortvisualizer.domain

import com.raffascript.sortvisualizer.core.util.ErrorMessage
import com.raffascript.sortvisualizer.core.util.Resource
import com.raffascript.sortvisualizer.data.preferences.UserPreferencesRepository

class ChangeListSizeUseCase(
    private val validateListSizeUseCase: ValidateListSizeUseCase,
    private val formatListSizeInputUseCase: FormatListSizeInputUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(listSize: Int): Resource<Unit> {
        if (validateListSizeUseCase(listSize)) {
            userPreferencesRepository.saveListSize(listSize)
            return Resource.Success(Unit)
        }
        return Resource.Failure(ErrorMessage.InvalidListSize)
    }

    suspend operator fun invoke(listSize: String): Resource<Unit> {
        val result = formatListSizeInputUseCase(listSize)
        return if (result is Resource.Success && result.data != null) {
            invoke(result.data)
        } else {
            Resource.Failure(result.error)
        }
    }
}