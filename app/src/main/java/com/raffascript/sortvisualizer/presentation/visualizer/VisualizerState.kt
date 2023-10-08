package com.raffascript.sortvisualizer.presentation.visualizer

import com.raffascript.sortvisualizer.data.AlgorithmState
import com.raffascript.sortvisualizer.data.preferences.UserPreferencesDataSource
import com.raffascript.sortvisualizer.data.viszualization.DelayValue
import com.raffascript.sortvisualizer.data.viszualization.Highlight

data class VisualizerState(
    // algorithmData
    val algorithmName: String,

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