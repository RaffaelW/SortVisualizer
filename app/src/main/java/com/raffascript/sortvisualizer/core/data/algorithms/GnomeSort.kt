package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class GnomeSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        var index = 0

        val size = list.size
        while (index < size.alsoIncComparisons()) {
            if (index == 0.alsoIncComparisons()) {
                index++
                defineStep(getHighlights(index))
            }

            if (list[index] >= list[index - 1].alsoIncBoth(2, 1)) {
                index++
            } else {
                swap(index, index - 1)
                index--
            }
            defineStep(getHighlights(index, index - 1))
        }
        defineEnd()
    }

    private fun getHighlights(firstIndex: Int, secondIndex: Int? = null): List<Highlight> {
        val highlights = listOf(
            firstIndex highlighted HighlightOption.COLOURED_PRIMARY,
        )
        return if (secondIndex != null) {
            highlights + (secondIndex highlighted HighlightOption.COLOURED_PRIMARY)
        } else highlights
    }
}