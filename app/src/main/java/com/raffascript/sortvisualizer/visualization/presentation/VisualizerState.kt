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
    val sortingList: IntArray = intArrayOf(),
    val highlights: List<Highlight> = emptyList(),
    val algorithmState: AlgorithmState = AlgorithmState.READY,
    val comparisonCount: Long = 0L,
    val arrayAccessCount: Long = 0L,

    // bottom sheet
    val showBottomSheet: Boolean = false,
    val sliderDelay: DelayValue = DelayValue.default,
    val isInputListSizeValid: Boolean = true,
    val listSize: Int = UserPreferencesDataSource.DEFAULT_LIST_SIZE,

    // other
    val isSoundOn: Boolean = true
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VisualizerState

        if (algorithmData != other.algorithmData) return false
        if (!sortingList.contentEquals(other.sortingList)) return false
        if (highlights != other.highlights) return false
        if (algorithmState != other.algorithmState) return false
        if (comparisonCount != other.comparisonCount) return false
        if (arrayAccessCount != other.arrayAccessCount) return false
        if (showBottomSheet != other.showBottomSheet) return false
        if (sliderDelay != other.sliderDelay) return false
        if (isInputListSizeValid != other.isInputListSizeValid) return false
        if (listSize != other.listSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = algorithmData.hashCode()
        result = 31 * result + sortingList.contentHashCode()
        result = 31 * result + highlights.hashCode()
        result = 31 * result + algorithmState.hashCode()
        result = 31 * result + comparisonCount.hashCode()
        result = 31 * result + arrayAccessCount.hashCode()
        result = 31 * result + showBottomSheet.hashCode()
        result = 31 * result + sliderDelay.hashCode()
        result = 31 * result + isInputListSizeValid.hashCode()
        result = 31 * result + listSize
        return result
    }
}