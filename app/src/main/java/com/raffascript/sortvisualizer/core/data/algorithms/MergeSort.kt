package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class MergeSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        val size = list.size
        val sortedList = mergeSort(list, 0, size - 1, defineStep)

        System.arraycopy(sortedList, 0, list, 0, size)
        defineEnd()
    }

    private suspend fun mergeSort(array: IntArray, left: Int, right: Int, defineStep: StepCallback): IntArray {
        if (left == right.alsoIncComparisons()) return intArrayOf(array[left].alsoIncArrayAccess())

        val middle = left + (right - left) / 2
        val leftArray = mergeSort(array, left, middle, defineStep)
        val rightArray = mergeSort(array, middle + 1, right, defineStep)
        updateListAndDefineStep(array, 0, left, right, defineStep)
        return merge(leftArray, rightArray, left, defineStep)
    }

    private suspend fun merge(
        leftArray: IntArray,
        rightArray: IntArray,
        position: Int, // only to know how to combine the start array with the right and left array to update the ui
        defineStep: StepCallback
    ): IntArray {
        val leftSize = leftArray.size
        val rightSize = rightArray.size

        val target = IntArray(leftSize + rightSize) { -1 }
        var targetPos = 0
        var leftPos = 0
        var rightPos = 0

        // wrapper function to avoid that the algorithm gets messy and unclear and the same code is repeated multiple times
        suspend fun combineAndDefineStep(secondaryIndex: Int) {
            val replacingArray = leftArray + rightArray
            val array = target.mapIndexed { index, value ->
                // don't count array access here because this is only for updating the UI and not for sorting
                if (value == -1) replacingArray[index]
                else value
            }.toIntArray()
            updateListAndDefineStep(
                array, position, position + targetPos, secondaryIndex, defineStep
            )
        }

        // While both arrays contain elements
        while (leftPos < leftSize && rightPos < rightSize.alsoIncComparisons(2)) {
            val leftValue = leftArray[leftPos].alsoIncArrayAccess()
            val rightValue = rightArray[rightPos].alsoIncArrayAccess()
            if (leftValue <= rightValue.alsoIncComparisons()) {
                target[targetPos] = leftValue.alsoIncArrayAccess()
                combineAndDefineStep(position + leftPos)
                targetPos++
                leftPos++
            } else {
                target[targetPos] = rightValue.alsoIncArrayAccess()
                combineAndDefineStep(position + rightPos)
                targetPos++
                rightPos++
            }
        }

        // copy the rest of the left array
        while (leftPos < leftSize.alsoIncComparisons()) {
            target[targetPos] = leftArray[leftPos].alsoIncArrayAccess(2)
            combineAndDefineStep(position + leftPos)
            targetPos++
            leftPos++
        }
        // copy the rest of the right array
        while (rightPos < rightSize.alsoIncComparisons()) {
            target[targetPos] = rightArray[rightPos].alsoIncArrayAccess(2)
            combineAndDefineStep(position + rightPos)
            targetPos++
            rightPos++
        }
        updateListAndDefineStep(target, position, position + targetPos, position + rightPos, defineStep)
        return target
    }

    private suspend fun updateListAndDefineStep(
        listPart: IntArray,
        partStart: Int,
        currentIndex: Int,
        secondIndex: Int,
        defineStep: StepCallback
    ) {
        System.arraycopy(listPart, 0, list, partStart, listPart.size)
        val highlights = mutableListOf(
            currentIndex highlighted HighlightOption.COLOURED_PRIMARY,
            secondIndex highlighted HighlightOption.COLOURED_PRIMARY
        )
        defineStep(highlights)
    }
}