package com.raffascript.sortvisualizer.data.algorithms

import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgress
import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgressHandler
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class MergeSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        val size = list.size
        val sortedList = mergeSort(list, 0, size - 1, progressHandler)
        System.arraycopy(sortedList, 0, list, 0, size)
        progressHandler.onFinish()
    }

    private fun mergeSort(array: IntArray, left: Int, right: Int, progressHandler: AlgorithmProgressHandler): IntArray {
        if (left == right) return intArrayOf(array[left])

        val middle = left + (right - left) / 2
        val leftArray = mergeSort(array, left, middle, progressHandler)
        val rightArray = mergeSort(array, middle + 1, right, progressHandler)
        return merge(leftArray, rightArray, progressHandler)
    }

    private fun merge(leftArray: IntArray, rightArray: IntArray, progressHandler: AlgorithmProgressHandler): IntArray {
        val leftSize = leftArray.size
        val rightSize = rightArray.size

        val target = IntArray(leftSize + rightSize)
        var targetPos = 0
        var leftPos = 0
        var rightPos = 0

        // While both arrays contain elements
        while (leftPos < leftSize && rightPos < rightSize) {
            val leftValue = leftArray[leftPos]
            var rightValue = rightArray[rightPos]
            if (leftValue <= rightValue) {
                target[targetPos++] = leftValue
                leftPos++
            } else {
                target[targetPos++] = rightValue
                rightPos++
            }
        }

        // copy the rest of the left array
        while (leftPos < leftSize) {
            target[targetPos++] = leftArray[leftPos++]
        }
        // copy the rest of the right array
        while (rightPos < rightSize) {
            target[targetPos++] = rightArray[rightPos++]
        }
        return target
    }
}