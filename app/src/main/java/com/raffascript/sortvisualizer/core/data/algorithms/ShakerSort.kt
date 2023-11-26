package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class ShakerSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        var swapped = true
        var startIndex = 0
        var endIndex = list.size - 1

        suspend fun swapIfUnordered(index: Int) {
            val left = list[index].alsoIncArrayAccess()
            val right = list[index + 1].alsoIncArrayAccess()
            if (left > right.alsoIncComparisons()) {
                list[index + 1] = left.alsoIncArrayAccess()
                list[index] = right.alsoIncArrayAccess()
                swapped = true
            }
            defineStep(getHighlights(index, index + 1, startIndex - 1, endIndex))
        }

        while (swapped) {
            swapped = false

            // loop from left to right
            for (i in startIndex until endIndex) {
                swapIfUnordered(i)
            }

            if (!swapped) {
                defineEnd()
                break
            }

            swapped = false
            endIndex-- // last element is sorted

            // loop from right to left
            for (i in (endIndex - 1) downTo startIndex) {
                swapIfUnordered(i)
            }
            startIndex++ // first element is sorted
        }
        defineEnd()
    }

    private fun getHighlights(firstIndex: Int, secondIndex: Int, leftLine: Int, rightLine: Int): List<Highlight> {
        return listOf(
            firstIndex highlighted HighlightOption.COLOURED_PRIMARY,
            secondIndex highlighted HighlightOption.COLOURED_PRIMARY,
            leftLine highlighted HighlightOption.LINE,
            rightLine highlighted HighlightOption.LINE
        )
    }
}