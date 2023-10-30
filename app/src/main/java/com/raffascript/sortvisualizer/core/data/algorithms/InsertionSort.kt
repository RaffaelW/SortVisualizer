package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.AlgorithmProgress
import com.raffascript.sortvisualizer.visualization.data.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class InsertionSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        for (i in 1 until list.size) {
            val item = list[i].alsoIncArrayAccess()
            var j = i
            progressHandler.onStep(*getHighlights(j, i))
            while (j > 0.alsoIncComparisons() && item < list[j - 1].alsoIncArrayAccess().alsoIncComparisons()) {
                list[j] = list[j - 1].alsoIncArrayAccess(2L)
                j--
                list[j] = item.alsoIncArrayAccess()
                progressHandler.onStep(*getHighlights(j, i))
            }
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