package com.raffascript.sortvisualizer.core.data.algorithms

class RadixSortMsd(list: IntArray) : RadixSortBase(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        val max = getMaximum()
        val numberOfDigits = getNumberOfDigits(max)

        sortByDigit(list, numberOfDigits - 1, defineStep)
        defineEnd()
    }

    override suspend fun sortByDigit(elements: IntArray, digitIndex: Int, defineStep: StepCallback) {
        val buckets = partition(elements, digitIndex, defineStep)

        if (digitIndex > 0) {
            for (bucket in buckets) {
                if (bucket.needsToBeSorted()) {
                    sortByDigit(bucket.getElements(), digitIndex - 1, defineStep)
//                    defineStep(getHighlights(digitIndex))
                }
            }
        }

        collect(elements, buckets, defineStep)
    }
}