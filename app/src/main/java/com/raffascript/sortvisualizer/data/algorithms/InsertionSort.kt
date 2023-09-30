package com.raffascript.sortvisualizer.data.algorithms

import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgress
import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.data.viszualization.Highlight
import com.raffascript.sortvisualizer.data.viszualization.HighlightOption
import com.raffascript.sortvisualizer.data.viszualization.highlighted
import kotlinx.coroutines.flow.FlowCollector
import kotlin.time.Duration

class InsertionSort(list: IntArray, delay: Duration) : Algorithm(list, delay) {

    override suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler) {
        for (i in 1 until list.size) {
            val item = list[i].andIncArrayAccess()
            var j = i
            progressHandler.onProgressChanged(*getHighlights(j, i))
            while (j > 0.andIncComparisons() && item < list[j - 1].andIncArrayAccess().andIncComparisons()) {
                list[j] = list[j - 1].andIncArrayAccess(2L)
                j--
                list[j] = item.andIncArrayAccess()
                progressHandler.onProgressChanged(*getHighlights(j, i))
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