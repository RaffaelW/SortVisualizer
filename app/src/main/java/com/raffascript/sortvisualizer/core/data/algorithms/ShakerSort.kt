package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.AlgorithmProgress
import com.raffascript.sortvisualizer.visualization.data.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class ShakerSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        var swapped = true
        var startIndex = 0
        var endIndex = list.size - 1

        suspend fun swapIfUnordered(index: Int, highlightedIndex: Int) {
            val left = list[index].alsoIncArrayAccess()
            val right = list[index + 1].alsoIncArrayAccess()
            if (left > right.alsoIncComparisons()) {
                list[index + 1] = left.alsoIncArrayAccess()
                list[index] = right.alsoIncArrayAccess()
                swapped = true
            }
            progressHandler.onStep(*getHighlights(highlightedIndex, startIndex - 1, endIndex))
        }

        while (swapped) {
            swapped = false

            // loop from left to right
            for (i in startIndex until endIndex) {
                swapIfUnordered(i, i + 1)
            }

            if (!swapped) {
                progressHandler.onFinish()
                break
            }

            swapped = false
            endIndex-- // last element is sorted

            // loop from right to left
            for (i in (endIndex - 1) downTo startIndex) {
                swapIfUnordered(i, i)
            }
            startIndex++ // first element is sorted
        }
        progressHandler.onFinish()
    }

    private fun getHighlights(primary: Int, leftLine: Int, rightLine: Int): Array<Highlight> {
        return arrayOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
            leftLine highlighted HighlightOption.LINE,
            rightLine highlighted HighlightOption.LINE
        )
    }
}