package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted
import kotlin.math.abs
import kotlin.math.pow

abstract class RadixSortBase(list: IntArray) : Algorithm(list) {

    protected fun getMaximum(): Int {
        var max = 0
        for (item in list) {
            if (item > max.alsoIncComparisons()) {
                max = item
            }
        }
        return max
    }

    protected fun getNumberOfDigits(number: Int): Int {
        return abs(number).toString().length
    }

    protected abstract suspend fun sortByDigit(elements: IntArray, digitIndex: Int, defineStep: StepCallback)
    protected suspend fun partition(elements: IntArray, digitIndex: Int, defineStep: StepCallback): List<Bucket> {
        val counts = countDigits(elements, digitIndex)
        val buckets = createBuckets(counts)
        distributeToBuckets(elements, digitIndex, buckets, defineStep)
        return buckets
    }

    private fun countDigits(elements: IntArray, digitIndex: Int): IntArray {
        val counts = IntArray(10)
        val divisor = calculateDivisor(digitIndex)
        for (item in elements) {
            val digit = item / divisor % 10
            counts[digit]++.alsoIncArrayAccess()
        }
        return counts
    }

    private fun createBuckets(counts: IntArray): List<Bucket> {
        return List(10) {
            Bucket(counts[it]).alsoIncArrayAccess()
        }
    }

    private suspend fun distributeToBuckets(
        elements: IntArray,
        digitIndex: Int,
        buckets: List<Bucket>,
        defineStep: StepCallback
    ) {
        val divisor = calculateDivisor(digitIndex)

        for ((index, item) in elements.withIndex()) {
            val digit = item / divisor % 10
            buckets[digit].add(item).alsoIncArrayAccess()
            defineStep(getHighlights(index))
        }
    }

    private fun calculateDivisor(digitIndex: Int): Int {
        return 10f.pow(digitIndex).toInt()
    }

    protected suspend fun collect(elements: IntArray, buckets: List<Bucket>, defineStep: StepCallback) {
        var targetIndex = 0
        for (bucket in buckets) {
            for (element in bucket.getElements()) {
                elements[targetIndex] = element.alsoIncArrayAccess()
                defineStep(getHighlights(targetIndex))
                targetIndex++
            }
        }
    }

    protected inner class Bucket(size: Int) {
        private val elements = IntArray(size) { -1 }
        private var addIndex = 0

        fun add(element: Int) {
            elements[addIndex] = element.alsoIncArrayAccess()
            addIndex++
        }

        fun getElements(): IntArray {
            return elements
        }

        fun needsToBeSorted(): Boolean {
            return elements.size > 1
        }
    }

    protected fun getHighlights(primary: Int): List<Highlight> {
        return listOf(primary highlighted HighlightOption.COLOURED_PRIMARY)
    }

}