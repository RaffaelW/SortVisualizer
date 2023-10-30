package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class BubbleSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: suspend (List<Highlight>) -> Unit, defineEnd: suspend () -> Unit) {
        for (max in list.lastIndex downTo 0) {
            var swapped = false
            for (i in 0 until max) {
                defineStep(getHighlights(i, max))
                val left = list[i].alsoIncArrayAccess()
                val right = list[i + 1].alsoIncArrayAccess()
                if (left > right.alsoIncComparisons()) {
                    list[i + 1] = left.alsoIncArrayAccess()
                    list[i] = right.alsoIncArrayAccess()
                    swapped = true
                }
            }
            defineStep(getHighlights(max, max))
            if (!swapped) break
        }
        defineEnd()
    }

    private fun getHighlights(primary: Int, line: Int): List<Highlight> {
        return listOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
    }
}