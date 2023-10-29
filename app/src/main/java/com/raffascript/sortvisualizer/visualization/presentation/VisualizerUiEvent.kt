package com.raffascript.sortvisualizer.visualization.presentation

import com.raffascript.sortvisualizer.visualization.data.DelayValue

sealed interface VisualizerUiEvent {
    // algorithm control
    object Play : VisualizerUiEvent
    object Pause : VisualizerUiEvent
    object Resume : VisualizerUiEvent
    object Restart : VisualizerUiEvent

    // bottom sheet
    object ShowBottomSheet: VisualizerUiEvent
    object HideBottomSheet: VisualizerUiEvent
    data class ChangeDelay(val delay: DelayValue) : VisualizerUiEvent
    data class ChangeListSizeInput(val input: String) : VisualizerUiEvent
}