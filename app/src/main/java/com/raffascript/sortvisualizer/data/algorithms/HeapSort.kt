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
        buildHeap()

        for (swapToPos in list.lastIndex downTo 1) {
            // move root to end
            swap(0, swapToPos)

            heapify(swapToPos, 0)
        }
    }

    private fun buildHeap() {
        val lastParentNode = list.size / 2 - 1
        for (i in lastParentNode downTo 0) {
            heapify(list.size, i)
        }
    }

    private fun heapify(length: Int, parentPos: Int) {
        while (true) {
            val leftChildPos = parentPos * 2 + 1
            val rightChildPos = parentPos * 2 + 2

            // find the largest element
            var largestPos = parentPos
            if (leftChildPos < length && list[leftChildPos] > list[parentPos]) {
                largestPos = leftChildPos
            }
            if (rightChildPos < length && list[rightChildPos] > list[parentPos]) {
                largestPos = rightChildPos
            }

            if (largestPos == parentPos) break

            swap(parentPos, largestPos)
        }
    }

    private fun swap(i: Int, j: Int) {
        val t = list[i]
        list[i] = list[j]
        list[j] = t
    }

    private fun getHighlights(primary: Int): Array<Highlight> {
        return arrayOf(primary highlighted HighlightOption.COLOURED_PRIMARY)
    }

}