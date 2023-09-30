package com.raffascript.sortvisualizer.presentation.visualizer

import com.raffascript.sortvisualizer.data.DelayValue

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