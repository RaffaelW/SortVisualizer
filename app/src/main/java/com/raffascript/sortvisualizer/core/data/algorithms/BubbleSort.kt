package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.AlgorithmProgress
import com.raffascript.sortvisualizer.visualization.data.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class BubbleSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        for (max in list.lastIndex downTo 0) {
            var swapped = false
            for (i in 0 until max) {
                progressHandler.onProgressChanged(*getHighlights(i, max))
                val left = list[i].alsoIncArrayAccess()
                val right = list[i + 1].alsoIncArrayAccess()
                if (left > right.alsoIncComparisons()) {
                    list[i + 1] = left.alsoIncArrayAccess()
                    list[i] = right.alsoIncArrayAccess()
                    swapped = true
                }
            }
            progressHandler.onProgressChanged(*getHighlights(max, max))
            if (!swapped) break
        }
        progressHandler.onFinish()
    }

    private fun getHighlights(primary: Int, line: Int): Array<Highlight> {
        return arrayOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
    }
}