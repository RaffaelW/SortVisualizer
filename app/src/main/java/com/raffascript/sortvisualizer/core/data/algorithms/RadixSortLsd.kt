package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted
import kotlin.math.pow

class RadixSortLsd(list: IntArray) : Algorithm(list) {

//    private val list = list.copyOf()

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        val max = getMaximum()
        val numberOfDigits = getNumberOfDigits(max)

        for (digitIndex in 0 until numberOfDigits) {
            val buckets = partition(digitIndex, defineStep)
            collect(buckets, defineStep)
        }
        defineEnd()
    }

    private fun getMaximum(): Int {
        var max = 0
        for (item in list) {
            if (item > max) {
                max = item
            }
        }
        return max
    }

    private fun getNumberOfDigits(number: Int): Int {
        val positiveNumber = if (number < 0) {
            number * -1
        } else number
        return positiveNumber.toString().length
    }

    private suspend fun partition(digitIndex: Int, defineStep: StepCallback): List<Bucket> {
        val counts = countDigits(digitIndex)
        val buckets = createBuckets(counts)
        distributeToBuckets(digitIndex, buckets, defineStep)
        return buckets
    }

    private fun countDigits(digitIndex: Int): IntArray {
        val counts = IntArray(10)
        val divisor = calculateDivisor(digitIndex)
        for (item in list) {
            val digit = item / divisor % 10
            counts[digit]++
        }
        return counts
    }

    private fun createBuckets(counts: IntArray): List<Bucket> {
        return List(10) {
            Bucket(counts[it])
        }
    }

    private suspend fun distributeToBuckets(digitIndex: Int, buckets: List<Bucket>, defineStep: StepCallback) {
        val divisor = calculateDivisor(digitIndex)

        for ((index, item) in list.withIndex()) {
            val digit = item / divisor % 10
            val bucket = buckets[digit]
            bucket.add(item)
            defineStep(getHighlights(index))
        }
    }

    private fun calculateDivisor(digitIndex: Int): Int {
        return 10f.pow(digitIndex).toInt()
    }

    private suspend fun collect(buckets: List<Bucket>, defineStep: StepCallback) {
        var targetIndex = 0
        for (bucket in buckets) {
            for (element in bucket.getElements()) {
                list[targetIndex] = element
                defineStep(getHighlights(targetIndex))
                targetIndex++
            }
        }
    }

    private class Bucket(size: Int) {
        private val elements = IntArray(size) { -1 }
        private var addIndex = 0

        fun add(element: Int) {
            elements[addIndex] = element
            addIndex++
        }

        fun getElements(): IntArray {
            return elements
        }
    }

    private fun getHighlights(primary: Int): List<Highlight> {
        return listOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
        )
    }

}