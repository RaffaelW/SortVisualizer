package com.raffascript.sortvisualizer.selection.presentation

import com.raffascript.sortvisualizer.core.data.AlgorithmData
import com.raffascript.sortvisualizer.core.data.TimeComplexity

data class SelectionState(
    val algorithmData: Map<TimeComplexity, List<AlgorithmData>>
)