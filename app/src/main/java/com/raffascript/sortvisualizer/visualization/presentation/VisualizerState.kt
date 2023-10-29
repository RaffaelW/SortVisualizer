package com.raffascript.sortvisualizer.visualization.presentation

import com.raffascript.sortvisualizer.core.data.AlgorithmData
import com.raffascript.sortvisualizer.visualization.data.AlgorithmState
import com.raffascript.sortvisualizer.visualization.data.DelayValue
import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesDataSource

data class VisualizerState(
    // algorithmData
    val algorithmData: AlgorithmData,

    // algorithm progress
    val sortingList: IntArray,
    val highlights: List<Highlight> = emptyList(),
    val algorithmState: AlgorithmState = AlgorithmState.READY,
    val comparisonCount: Long = 0L,
    val arrayAccessCount: Long = 0L,

    // bottom sheet
    val showBottomSheet: Boolean = false,
    val sliderDelay: DelayValue = DelayValue.default,
    val isInputListSizeValid: Boolean = true,
    val listSize: Int = UserPreferencesDataSource.DEFAULT_LIST_SIZE,
)