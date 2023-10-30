package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class SelectionSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        for (i in 0 until list.lastIndex) {
            var minPos = i
            var min = list[minPos].alsoIncArrayAccess()
            for (j in i + 1..list.lastIndex) {
                defineStep(getHighlights(j, minPos, i))
                if (list[j] < min.alsoIncArrayAccess().alsoIncComparisons()) {
                    minPos = j
                    min = list[minPos].alsoIncArrayAccess()
                }
            }

            if (minPos != i) {
                list[minPos] = list[i].alsoIncArrayAccess(2L)
                list[i] = min.alsoIncArrayAccess()
            }
            defineStep(getHighlights(i, null, i))
        }
        defineEnd()
    }

    private fun getHighlights(primary: Int, secondary: Int?, line: Int): List<Highlight> {
        val highlights = mutableListOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
        if (secondary != null) {
            highlights += secondary highlighted HighlightOption.COLOURED_SECONDARY
        }
        return highlights
    }

}