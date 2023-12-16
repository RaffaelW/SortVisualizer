package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class PancakeSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        val size = list.size
        for (currentSize in size downTo 2) {
            val maximum = findIndexOfMax(currentSize)
            if (maximum != currentSize - 1.alsoIncComparisons()) {
                flip(maximum, defineStep)
                flip(currentSize - 1, defineStep)
            }
        }

        defineEnd()
    }

    private fun findIndexOfMax(lastIndex: Int): Int {
        var maxIndex = 0
        var i = 1
        while (i < lastIndex.alsoIncComparisons()) {
            if (list[i] > list[maxIndex].alsoIncBoth(2, 1)) {
                maxIndex = i
            }
            i++
        }
        return maxIndex
    }

    private suspend fun flip(endIndex: Int, defineStep: StepCallback) {
        var start = 0
        var end = endIndex
        while (start < endIndex.alsoIncComparisons()) {
            swap(start, end)

            defineStep(getHighlights(start, end, endIndex))

            start++
            end--
        }
    }

    private fun getHighlights(firstIndex: Int, secondIndex: Int, line: Int): List<Highlight> {
        return listOf(
            firstIndex highlighted HighlightOption.COLOURED_PRIMARY,
            secondIndex highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
    }
}