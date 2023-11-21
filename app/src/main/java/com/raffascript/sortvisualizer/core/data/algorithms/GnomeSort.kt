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

            if (list[index] >= list[index - 1].alsoIncArrayAccess(2).alsoIncComparisons()) {
                index++
            } else {
                val temp = list[index].alsoIncArrayAccess()
                list[index] = list[index - 1].alsoIncArrayAccess(2)
                list[index - 1] = temp.alsoIncArrayAccess()
                index--
            }
            defineStep(getHighlights(index))
        }
        defineEnd()
    }

    private fun getHighlights(primary: Int): List<Highlight> {
        return listOf(primary highlighted HighlightOption.COLOURED_PRIMARY)
    }
}