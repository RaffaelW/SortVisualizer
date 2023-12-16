package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class OddEvenSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        var isSorted = false

        while (!isSorted) {
            val swappedOdd = bubbleSort(1, defineStep)
            val swappedEven = bubbleSort(0, defineStep)
            isSorted = !swappedOdd && !swappedEven
        }

        defineEnd()
    }

    private suspend fun bubbleSort(startIndex: Int, defineStep: StepCallback): Boolean {
        val size = list.size
        var swapped = false

        for (i in startIndex until (size - 1) step 2) {
            if (list[i] > list[i + 1].alsoIncBoth(2, 1)) {
                swap(i, i + 1)
                swapped = true
            }
            defineStep(getHighlights(i, i + 1))
        }

        return swapped
    }

    private fun getHighlights(firstIndex: Int, secondIndex: Int): List<Highlight> {
        return listOf(
            firstIndex highlighted HighlightOption.COLOURED_PRIMARY,
            secondIndex highlighted HighlightOption.COLOURED_PRIMARY
        )
    }
}