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
                defineStep(getHighlights(i, left, right))
            }

            while (j > left && list[j] >= pivot.alsoIncBoth(1, 2)) {
                j--
                defineStep(getHighlights(j, left, right))
            }

            if (i < j.alsoIncComparisons()) {
                swap(i, j)
                defineStep(getHighlights(i, left, right))
                i++
                j--
            }
        }

        if (i == j && list[i] < pivot.alsoIncBoth(1, 2)) {
            i++
            defineStep(getHighlights(i, left, right))
        }

        if (list[i] != pivot.alsoIncBoth()) {
            swap(i, right)
            defineStep(getHighlights(i, left,right))
        }
        return i
    }

    private fun swap(left: Int, right: Int) {
        val temp = list[left]
        list[left] = list[right]
        list[right] = temp
        alsoIncArrayAccess(4)
    }

    private fun getHighlights(primary: Int, line1: Int, line2: Int): List<Highlight> {
        return listOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
            line1 highlighted HighlightOption.LINE,
            line2 highlighted HighlightOption.LINE
        )
    }
}