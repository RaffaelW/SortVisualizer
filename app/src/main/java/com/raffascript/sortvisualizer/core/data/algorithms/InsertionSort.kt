package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class InsertionSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        for (i in 1 until list.size) {
            val item = list[i].alsoIncArrayAccess()
            var j = i
            defineStep(getHighlights(j, j - 1, i))
            while (j > 0 && item < list[j - 1].alsoIncBoth(1, 2)) {
                list[j] = list[j - 1].alsoIncArrayAccess(2)
                j--
                list[j] = item.alsoIncArrayAccess()
                defineStep(getHighlights(j, j - 1, i))
            }
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