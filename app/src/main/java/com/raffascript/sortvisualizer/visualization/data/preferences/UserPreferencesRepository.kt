package com.raffascript.sortvisualizer.visualization.data.preferences

import com.raffascript.sortvisualizer.visualization.data.DelayValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class UserPreferencesRepository(
    private val preferencesDataSource: UserPreferencesDataSource
) {

    fun getUserPreferencesFlow(): Flow<UserPreferences> {
        return combine(
            preferencesDataSource.delayFlow,
            preferencesDataSource.listSizeFlow,
            preferencesDataSource.soundFlow
        ) { delay, listSize, isSoundEnabled ->
            UserPreferences(delay, listSize, isSoundEnabled)
        }
    }

    suspend fun saveDelay(delayValue: DelayValue) {
        preferencesDataSource.setDelay(delayValue)
    }

    suspend fun saveListSize(listSize: Int) {
        preferencesDataSource.setListSize(listSize)
    }

    suspend fun saveSoundEnabled(isSoundEnabled: Boolean) {
        preferencesDataSource.setSound(isSoundEnabled)
    }
}