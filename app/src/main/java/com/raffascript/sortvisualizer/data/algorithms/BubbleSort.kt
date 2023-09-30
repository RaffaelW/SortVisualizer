package com.raffascript.sortvisualizer.data.algorithms

import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgress
import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.data.viszualization.Highlight
import com.raffascript.sortvisualizer.data.viszualization.HighlightOption
import com.raffascript.sortvisualizer.data.viszualization.highlighted
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class BubbleSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        for (max in list.lastIndex downTo 0) {
            var swapped = false
            for (i in 0 until max) {
                progressHandler.onProgressChanged(*getHighlights(i, max))
                val left = list[i].andIncArrayAccess()
                val right = list[i + 1].andIncArrayAccess()
                if (left > right.andIncComparisons()) {
                    list[i + 1] = left.andIncArrayAccess()
                    list[i] = right.andIncArrayAccess()
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