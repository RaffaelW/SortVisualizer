package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.AlgorithmProgress
import com.raffascript.sortvisualizer.visualization.data.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class HeapSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        buildHeap(progressHandler)

        for (swapToPos in list.lastIndex downTo 1) {
            // move root to end
            swap(0, swapToPos)
            progressHandler.onStep(*getHighlights(swapToPos, swapToPos - 1))

            heapify(swapToPos, 0, progressHandler)
        }

        progressHandler.onFinish()
    }

    private suspend fun buildHeap(progressHandler: AlgorithmProgressHandler) {
        val lastParentNode = list.size / 2 - 1
        for (i in lastParentNode downTo 0) {
            heapify(list.size, i, progressHandler)
            progressHandler.onStep(*getHighlights(i, list.size))
        }
    }

    private suspend fun heapify(length: Int, parentPos: Int, progressHandler: AlgorithmProgressHandler) {
        @Suppress("NAME_SHADOWING") var parentPos = parentPos
        while (true) {
            val leftChildPos = parentPos * 2 + 1
            val rightChildPos = parentPos * 2 + 2

            // find the largest element
            var largestPos = parentPos
            if (leftChildPos < length.alsoIncComparisons()
                && list[leftChildPos] > list[largestPos].alsoIncComparisons().alsoIncArrayAccess(2L)
            ) {
                largestPos = leftChildPos
            }
            if (rightChildPos < length.alsoIncComparisons()
                && list[rightChildPos] > list[largestPos].alsoIncComparisons().alsoIncArrayAccess(2L)
            ) {
                largestPos = rightChildPos
            }

            if (largestPos == parentPos.alsoIncComparisons()) {
                progressHandler.onStep(*getHighlights(parentPos, length - 1))
                break
            }

            swap(parentPos, largestPos)
            progressHandler.onStep(*getHighlights(parentPos, length - 1))

            parentPos = largestPos
        }
    }

    private fun swap(i: Int, j: Int) {
        val t = list[i]
        list[i] = list[j]
        list[j] = t
        alsoIncArrayAccess(4L)
    }

    private fun getHighlights(primary: Int, line: Int): Array<Highlight> {
        return arrayOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
    }

}