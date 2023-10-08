package com.raffascript.sortvisualizer.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.raffascript.sortvisualizer.data.DelayValue
import kotlinx.coroutines.flow.map

class UserPreferencesDataSource(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")

    private val delayKey = intPreferencesKey("delay")
    private val listSizeKey = intPreferencesKey("list_size")

    val delayFlow = context.dataStore.data.map {
        val ordinal = it[delayKey]
        ordinal?.let {
            DelayValue.values()[ordinal]
        } ?: DelayValue.default
    }

    val listSizeFlow = context.dataStore.data.map {
        it[listSizeKey] ?: DEFAULT_LIST_SIZE
    }

    suspend fun setDelay(delay: DelayValue) {
        context.dataStore.edit {
            it[delayKey] = delay.ordinal
        }
    }

    suspend fun setListSize(listSize: Int) {
        context.dataStore.edit {
            it[listSizeKey] = listSize
        }
    }

    companion object {
        const val DEFAULT_LIST_SIZE = 50
    }
}