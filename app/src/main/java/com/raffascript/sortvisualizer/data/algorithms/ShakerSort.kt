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

        while (swapped) {
            swapped = false

            // loop from left to right
            for (i in startIndex until endIndex) {
                val left = list[i]
                val right = list[i + 1]
                if (left > right) {
                    list[i + 1] = left
                    list[i] = right
                    swapped = true
                }
            }

            if (!swapped) {
                progressHandler.onFinish()
                break
            }
            swapped = false

            // last element is sorted
            endIndex--

            // loop from right to left
            for (i in (endIndex - 1) downTo startIndex) {
                val left = list[i]
                val right = list[i + 1]
                if (left > right) {
                    list[i + 1] = left
                    list[i] = right
                    swapped = true
                }
            }

            // first element sorted
            startIndex++
        }

    }
}