package com.raffascript.sortvisualizer.data.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class UserPreferencesRepository(
    private val preferencesDataSource: UserPreferencesDataSource
) {

    fun getUserPreferencesFlow(): Flow<UserPreferences> {
        return combine(preferencesDataSource.delayFlow, preferencesDataSource.listSizeFlow) { delay, listSize ->
            UserPreferences(delay, listSize)
        }
    }

    suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        val (delay, listSize) = userPreferences
        preferencesDataSource.apply {
            setDelay(delay)
            setListSize(listSize)
        }
    }
}