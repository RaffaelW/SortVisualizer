package com.raffascript.sortvisualizer.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.raffascript.sortvisualizer.data.DelayValue
import kotlinx.coroutines.flow.map

class UserPreferencesDataSource(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")

    private val delayKey = intPreferencesKey("delay")
    private val listSizeKey = intPreferencesKey("list_size")
    private val inputListSizeKey = stringPreferencesKey("input_list_size")

    val delayFlow = context.dataStore.data.map {
        val ordinal = it[delayKey]
        ordinal?.let {
            DelayValue.values()[ordinal]
        } ?: DelayValue.default
    }

    val listSizeFlow = context.dataStore.data.map {
        it[listSizeKey] ?: DEFAULT_LIST_SIZE
    }

    val inputListSizeFlow = context.dataStore.data.map {
        it[inputListSizeKey] ?: DEFAULT_LIST_SIZE.toString()
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

    suspend fun setInputListSize(inputListSize: String) {
        context.dataStore.edit {
            it[inputListSizeKey] = inputListSize
        }
    }

    companion object {
        const val DEFAULT_LIST_SIZE = 50
    }
}