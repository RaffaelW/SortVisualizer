package com.raffascript.sortvisualizer.data.algorithms

import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgress
import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.data.viszualization.HighlightOption
import com.raffascript.sortvisualizer.data.viszualization.highlighted
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class MergeSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        val size = list.size
        val sortedList = mergeSort(list, 0, size - 1, progressHandler)
        finish(sortedList, progressHandler)
    }

    private suspend fun mergeSort(
        array: IntArray,
        left: Int,
        right: Int,
        progressHandler: AlgorithmProgressHandler
    ): IntArray {
        if (left == right) return intArrayOf(array[left])

        val middle = left + (right - left) / 2
        val leftArray = mergeSort(array, left, middle, progressHandler)
        val rightArray = mergeSort(array, middle + 1, right, progressHandler)
        updateProgress(array, 0, left, right, progressHandler)
        return merge(leftArray, rightArray, left, progressHandler)
    }

    private suspend fun merge(
        leftArray: IntArray,
        rightArray: IntArray,
        position: Int, // only to know how to combine the start array with the right and left array to update the ui
        progressHandler: AlgorithmProgressHandler
    ): IntArray {
        val leftSize = leftArray.size
        val rightSize = rightArray.size

        val target = IntArray(leftSize + rightSize)
        var targetPos = 0
        var leftPos = 0
        var rightPos = 0

        // While both arrays contain elements
        while (leftPos < leftSize && rightPos < rightSize) {
            val leftValue = leftArray[leftPos]
            val rightValue = rightArray[rightPos]
            if (leftValue <= rightValue) {
                target[targetPos] = leftValue
                updateProgress(leftArray + rightArray, position, targetPos, leftPos, progressHandler)
                targetPos++
                leftPos++
            } else {
                target[targetPos] = rightValue
                updateProgress(leftArray + rightArray, position, targetPos, rightPos, progressHandler)
                targetPos++
                rightPos++
            }
        }

        // copy the rest of the left array
        while (leftPos < leftSize) {
            target[targetPos] = leftArray[leftPos]
            updateProgress(leftArray + rightArray, position, targetPos, leftPos, progressHandler)
            targetPos++
            leftPos++
        }
        // copy the rest of the right array
        while (rightPos < rightSize) {
            target[targetPos] = rightArray[rightPos]
            updateProgress(leftArray + rightArray, position, targetPos, rightPos, progressHandler)
            targetPos++
            rightPos++
        }
        return target
    }

    private suspend fun updateProgress(
        listPart: IntArray,
        partStart: Int,
        currentIndex: Int,
        secondaryIndex: Int,
        progressHandler: AlgorithmProgressHandler
    ) {
        System.arraycopy(listPart, 0, list, partStart, listPart.size)
        progressHandler.onProgressChanged(
            currentIndex highlighted HighlightOption.COLOURED_PRIMARY,
            secondaryIndex highlighted HighlightOption.COLOURED_SECONDARY
        )
    }

    private suspend fun finish(finalList: IntArray, progressHandler: AlgorithmProgressHandler) {
        System.arraycopy(finalList, 0, list, 0, finalList.size)
        progressHandler.onFinish()
    }
}