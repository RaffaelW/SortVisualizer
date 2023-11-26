package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class BubbleSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        for (max in list.lastIndex downTo 0) {
            var swapped = false
            for (i in 0 until max) {
                val left = list[i]
                val right = list[i + 1]
                alsoIncArrayAccess(2)
                if (left > right.alsoIncComparisons()) {
                    list[i + 1] = left
                    list[i] = right
                    alsoIncArrayAccess(2)
                    swapped = true
                }
                defineStep(getHighlights(i, i + 1, max))
            }
            if (!swapped) break
        }
        defineEnd()
    }

    private fun getHighlights(firstIndex: Int, secondIndex: Int, line: Int): List<Highlight> {
        return listOf(
            firstIndex highlighted HighlightOption.COLOURED_PRIMARY,
            secondIndex highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
    }
}