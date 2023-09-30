package com.raffascript.sortvisualizer.presentation.visualizer

import com.raffascript.sortvisualizer.data.DelayValue
import com.raffascript.sortvisualizer.data.AlgorithmState
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
    val isBottomSheetShown: Boolean = false,
    val sliderDelay: DelayValue = DelayValue.DELAY_10,
    val inputListSize: String = "50",
    val isInputListSizeValid: Boolean = true,
    val listSize: Int = 50,
)