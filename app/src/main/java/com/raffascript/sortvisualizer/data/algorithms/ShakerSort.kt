package com.raffascript.sortvisualizer.data.algorithms

import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgress
import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgressHandler
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class ShakerSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        var swapped = true
        var startIndex = 0
        var endIndex = list.size - 1

        fun swapIfUnordered(index: Int) {
            val left = list[index]
            val right = list[index + 1]
            if (left > right) {
                list[index + 1] = left
                list[index] = right
                swapped = true
            }
        }

        while (swapped) {
            swapped = false

            // loop from left to right
            for (i in startIndex until endIndex) {
                swapIfUnordered(i)
            }

            if (!swapped) {
                progressHandler.onFinish()
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

    }
}