package com.raffascript.sortvisualizer.data.preferences

import com.raffascript.sortvisualizer.data.viszualization.DelayValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class UserPreferencesRepository(
    private val preferencesDataSource: UserPreferencesDataSource
) {

    fun getUserPreferencesFlow(): Flow<UserPreferences> {
        return combine(
            preferencesDataSource.delayFlow,
            preferencesDataSource.listSizeFlow,
        ) { delay, listSize ->
            UserPreferences(delay, listSize)
        }
    }

    suspend fun saveDelay(delayValue: DelayValue) {
        preferencesDataSource.setDelay(delayValue)
    }

    suspend fun saveListSize(listSize: Int) {
        preferencesDataSource.setListSize(listSize)
    }
}