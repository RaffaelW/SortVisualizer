package com.raffascript.sortvisualizer.data.algorithms

import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgress
import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.data.viszualization.Highlight
import com.raffascript.sortvisualizer.data.viszualization.HighlightOption
import com.raffascript.sortvisualizer.data.viszualization.highlighted
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class HeapSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        buildHeap(progressHandler)

        for (swapToPos in list.lastIndex downTo 1) {
            // move root to end
            swap(0, swapToPos)
            progressHandler.onProgressChanged(*getHighlights(swapToPos))

            heapify(swapToPos, 0, progressHandler)
        }

        progressHandler.onFinish()
    }

    private suspend fun buildHeap(progressHandler: AlgorithmProgressHandler) {
        val lastParentNode = list.size / 2 - 1
        for (i in lastParentNode downTo 0) {
            heapify(list.size, i, progressHandler)
            progressHandler.onProgressChanged(*getHighlights(i))
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
                && list[leftChildPos] > list[parentPos].alsoIncComparisons().alsoIncArrayAccess(2L)
            ) {
                largestPos = leftChildPos
            }
            if (rightChildPos < length.alsoIncComparisons()
                && list[rightChildPos] > list[parentPos].alsoIncComparisons().alsoIncArrayAccess(2L)
            ) {
                largestPos = rightChildPos
            }

            if (largestPos == parentPos.alsoIncComparisons()) {
                progressHandler.onProgressChanged(*getHighlights(parentPos))
                break
            }

            swap(parentPos, largestPos)
            progressHandler.onProgressChanged(*getHighlights(parentPos))

            parentPos = largestPos
        }
    }

    private fun swap(i: Int, j: Int) {
        val t = list[i]
        list[i] = list[j]
        list[j] = t
        alsoIncArrayAccess(4L)
    }

    private fun getHighlights(primary: Int): Array<Highlight> {
        return arrayOf(primary highlighted HighlightOption.COLOURED_PRIMARY)
    }

}