package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class QuickSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        quickSort(0, list.size - 1, defineStep)
        defineEnd()
    }

    private suspend fun quickSort(left: Int, right: Int, defineStep: StepCallback) {
        if (left >= right) return

        val pivotPos = partition(left, right, defineStep)
        quickSort(left, pivotPos - 1, defineStep)
        quickSort(pivotPos + 1, right, defineStep)
    }

    private suspend fun partition(left: Int, right: Int, defineStep: StepCallback): Int {
        val pivot = list[right].alsoIncArrayAccess()

        var i = left
        var j = right - 1
        while (i < j.alsoIncComparisons()) {

            while (list[i] < pivot.alsoIncBoth()) {
                i++
                defineStep(getHighlights(i, null, right, pivot, left))
            }

            while (j > left && list[j] >= pivot.alsoIncBoth(1, 2)) {
                j--
                defineStep(getHighlights(j, null, right, pivot, left))
            }

            if (i < j.alsoIncComparisons()) {
                swap(i, j)
                defineStep(getHighlights(i, j, right, pivot, left))
                i++
                j--
            }
        }

        if (i == j && list[i] < pivot.alsoIncBoth(1, 2)) {
            i++
            defineStep(getHighlights(i, null, right, pivot, left))
        }

        if (list[i] != pivot.alsoIncBoth()) {
            swap(i, right)
            defineStep(getHighlights(i, j, right, pivot, left))
        }
        return i
    }

    private fun getHighlights(
        firstIndex: Int,
        secondIndex: Int?,
        line1: Int,
        line2: Int,
        pivot: Int
    ): List<Highlight> {
        val highlights = listOf(
            firstIndex highlighted HighlightOption.COLOURED_PRIMARY,
            pivot highlighted HighlightOption.COLOURED_SECONDARY,
            line1 highlighted HighlightOption.LINE,
            line2 highlighted HighlightOption.LINE
        )
        if (secondIndex != null) {
            return highlights + (secondIndex highlighted HighlightOption.COLOURED_PRIMARY)
        }
        return highlights
    }
}