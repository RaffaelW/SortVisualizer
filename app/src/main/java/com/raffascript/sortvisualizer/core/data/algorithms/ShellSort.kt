package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class ShellSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        val n = list.size

        var gap = n / 2
        while (gap > 0.alsoIncComparisons()) {

            var i = gap
            while (i < n.alsoIncComparisons()) {
                val temp = list[i].alsoIncArrayAccess()

                var j = i
                while (j >= gap && list[j - gap] > temp.alsoIncBoth(1, 2)) {
                    list[j] = list[j - gap].alsoIncArrayAccess(2)
                    defineStep(getHighlights(j, j - gap, i))
                    j -= gap
                }

                list[j] = temp.alsoIncArrayAccess()
                defineStep(getHighlights(j, i, i))
                i+= 1
            }
            defineStep(getHighlights(i, null, i))
            gap /= 2
        }

        defineEnd()
    }

    private fun getHighlights(firstIndex: Int, secondIndex: Int?, line: Int): List<Highlight> {
        val highlights = listOf(
            firstIndex highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
        if (secondIndex != null) {
            return highlights + (secondIndex highlighted HighlightOption.COLOURED_PRIMARY)
        }
        return highlights
    }

}