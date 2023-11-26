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
                if (list[j] < min.alsoIncBoth()) {
                    minPos = j
                    min = list[minPos].alsoIncArrayAccess()
                }
                defineStep(getHighlights(j, null, minPos, i))
            }

            if (minPos != i.alsoIncComparisons()) {
                list[minPos] = list[i].alsoIncArrayAccess(2)
                list[i] = min.alsoIncArrayAccess()
            }
            defineStep(getHighlights(i, minPos,null, i))
        }
        defineEnd()
    }

    private fun getHighlights(firstIndex: Int, secondIndex: Int?, min: Int?, line: Int): List<Highlight> {
        val highlights = mutableListOf(
            firstIndex highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
        if (secondIndex != null) {
            highlights += secondIndex highlighted HighlightOption.COLOURED_SECONDARY
        }
        if (min != null) {
            highlights += min highlighted HighlightOption.COLOURED_SECONDARY
        }
        return highlights
    }

}