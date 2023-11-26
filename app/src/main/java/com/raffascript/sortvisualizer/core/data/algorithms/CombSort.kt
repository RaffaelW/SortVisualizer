package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class CombSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        val n = list.size
        var gap = n
        var isSwapped = true

        while (gap != 1 || isSwapped.alsoIncComparisons()) {
            gap = nextGap(gap)
            isSwapped = false

            for (i in 0 until n - gap) {
                if (list[i] > list[i + gap].alsoIncBoth(2, 1)) {
                    swap(i, i + gap)
                    isSwapped = true
                }
                defineStep(getHighlights(i, i + gap))
            }
        }
        defineEnd()
    }

    private fun nextGap(gap: Int): Int {
        val nextGap = (gap * 10) / 13
        return if (nextGap < 1.alsoIncComparisons()) 1 else nextGap
    }

    private fun getHighlights(firstIndex: Int, secondIndex: Int): List<Highlight> {
        return listOf(
            firstIndex highlighted HighlightOption.COLOURED_PRIMARY,
            secondIndex highlighted HighlightOption.COLOURED_PRIMARY
        )
    }

}