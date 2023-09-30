package com.raffascript.sortvisualizer.data.algorithms

import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgress
import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.data.viszualization.Highlight
import com.raffascript.sortvisualizer.data.viszualization.HighlightOption
import com.raffascript.sortvisualizer.data.viszualization.highlighted
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class SelectionSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        for (i in 0 until list.lastIndex) {
            var minPos = i
            var min = list[minPos]
            for (j in i + 1..list.lastIndex) {
                progressHandler.onProgressChanged(*getHighlights(j, minPos, i))
                if (list[j] < min) {
                    minPos = j
                    min = list[minPos]
                }
            }

            if (minPos != i) {
                list[minPos] = list[i]
                list[i] = min
            }
            progressHandler.onProgressChanged(*getHighlights(i, null, i))
        }
        progressHandler.onFinish()
    }

    private fun getHighlights(primary: Int, secondary: Int?, line: Int): Array<Highlight> {
        var highlights = arrayOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
        if (secondary != null) {
            highlights += secondary highlighted HighlightOption.COLOURED_SECONDARY
        }
        return highlights
    }

}