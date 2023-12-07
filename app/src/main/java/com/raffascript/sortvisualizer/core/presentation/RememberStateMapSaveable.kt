package com.raffascript.sortvisualizer.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap

@Composable
fun <K, V> rememberStateMapSaveable(init: () -> SnapshotStateMap<K, V>): SnapshotStateMap<K, V> {
    return rememberSaveable(saver = listSaver(
        save = { stateMap ->
            val value = stateMap.values.firstOrNull() ?: 0
            if (!canBeSaved(value)) {
                throw IllegalStateException("${value::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
            }
            stateMap.toList()
        },
        restore = {
            it.toMutableStateMap()
        }
    )) {
        init()
    }
}