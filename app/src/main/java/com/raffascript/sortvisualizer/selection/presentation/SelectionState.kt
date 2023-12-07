package com.raffascript.sortvisualizer.selection.presentation

import com.raffascript.sortvisualizer.core.data.AlgorithmData
import com.raffascript.sortvisualizer.core.data.AlgorithmGroup

data class SelectionState(
    val algorithmData: Map<AlgorithmGroup, List<AlgorithmData>>
)