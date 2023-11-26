package com.raffascript.sortvisualizer.core.data.algorithms

class RadixSortLsd(list: IntArray) : RadixSortBase(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        val max = getMaximum()
        val numberOfDigits = getNumberOfDigits(max)

        for (digitIndex in 0 until numberOfDigits) {
            sortByDigit(list, digitIndex, defineStep)
        }
        defineEnd()
    }

    override suspend fun sortByDigit(elements: IntArray, digitIndex: Int, defineStep: StepCallback) {
        val buckets = partition(elements, digitIndex, defineStep)
        collect(elements, buckets, defineStep)
    }
}