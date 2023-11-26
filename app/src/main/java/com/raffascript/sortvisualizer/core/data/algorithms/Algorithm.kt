package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight

typealias StepCallback = suspend (List<Highlight>) -> Unit

abstract class Algorithm(protected var list: IntArray) {

    private var arrayAccesses = 0L
    private var comparisons = 0L

    val listSize: Int
        get() = list.size

    suspend fun startSorting(
        onStep: suspend (IntArray, List<Highlight>, Long, Long) -> Unit,
        onFinish: suspend (IntArray, Long, Long) -> Unit
    ) {
        sort(
            defineStep = { highlights ->
                onStep(list, highlights, arrayAccesses, comparisons)
            },
            defineEnd = {
                onFinish(list, arrayAccesses, comparisons)
            }
        )
    }

    protected abstract suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit)

    protected fun swap(left: Int, right: Int) {
        val temp = list[left]
        list[left] = list[right]
        list[right] = temp
        alsoIncArrayAccess(4)
    }

    protected fun <T> T.alsoIncArrayAccess(number: Long = 1): T {
        arrayAccesses += number
        return this
    }

    protected fun <T> T.alsoIncComparisons(number: Long = 1): T {
        comparisons += number
        return this
    }

    protected fun <T> T.alsoIncBoth(): T {
        alsoIncBoth(1, 1)
        return this
    }

    protected fun <T> T.alsoIncBoth(arrayAccesses: Long, comparisons: Long): T {
        this@Algorithm.arrayAccesses += arrayAccesses
        this@Algorithm.comparisons += comparisons
        return this
    }
}