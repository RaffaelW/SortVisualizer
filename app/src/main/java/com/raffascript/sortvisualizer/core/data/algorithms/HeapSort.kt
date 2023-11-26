package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class HeapSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        buildHeap(defineStep)

        for (swapToPos in list.lastIndex downTo 1) {
            // move root to end
            swap(0, swapToPos)
            defineStep(getHighlights(0, swapToPos, swapToPos - 1))

            heapify(swapToPos, 0, defineStep)
        }

        defineEnd()
    }

    private suspend fun buildHeap(defineStep: StepCallback) {
        val lastParentNode = list.size / 2 - 1
        for (i in lastParentNode downTo 0) {
            heapify(list.size, i, defineStep)
        }
    }

    private suspend fun heapify(length: Int, parentPos: Int, defineStep: StepCallback) {
        @Suppress("NAME_SHADOWING") var parentPos = parentPos
        while (true) {
            val leftChildPos = parentPos * 2 + 1
            val rightChildPos = parentPos * 2 + 2

            // find the largest element
            var largestPos = parentPos
            if (leftChildPos < length && list[leftChildPos] > list[largestPos].alsoIncBoth(2, 2)) {
                largestPos = leftChildPos
            }
            if (rightChildPos < length && list[rightChildPos] > list[largestPos].alsoIncBoth(2, 2)) {
                largestPos = rightChildPos
            }

            if (largestPos == parentPos.alsoIncComparisons()) {
                defineStep(getHighlights(parentPos, null, length - 1))
                break
            }

            swap(parentPos, largestPos)
            defineStep(getHighlights(parentPos, largestPos, length - 1))

            parentPos = largestPos
        }
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