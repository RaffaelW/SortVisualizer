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
                    defineStep(getHighlights(j, i))
                    list[j] = list[j - gap].alsoIncArrayAccess(2)
                    j -= gap
                }

                defineStep(getHighlights(j, i))
                list[j] = temp.alsoIncArrayAccess()
                i+= 1
            }
            defineStep(getHighlights(i, i))
            gap /= 2
        }

        defineEnd()
    }

    private fun getHighlights(primary: Int, firstLine: Int): List<Highlight> {
        return listOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
            firstLine highlighted HighlightOption.LINE
        )
    }

}