package com.raffascript.sortvisualizer.core.data.algorithms

import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.highlighted

class CycleSort(list: IntArray) : Algorithm(list) {

    override suspend fun sort(defineStep: StepCallback, defineEnd: suspend () -> Unit) {
        val n = list.size
        for (cycleStart in 0..n - 2) {
            var item = list[cycleStart].alsoIncArrayAccess()

            // Find position where we put the item. We basically
            // count all smaller elements on right side of item.
            var pos = cycleStart
            for (i in cycleStart + 1 until n) {
                if (list[i] < item.alsoIncBoth()) {
                    pos++
                }
                defineStep(getHighlights(i, cycleStart, pos))
            }

            // If item is already in correct position
            if (pos == cycleStart.alsoIncComparisons()) continue

            // ignore all duplicate elements
            while (item == list[pos].alsoIncBoth()) {
                pos++
                defineStep(getHighlights(pos, cycleStart))
            }

            if (pos != cycleStart.alsoIncComparisons()) {
                val temp = item
                item = list[pos].alsoIncArrayAccess()
                list[pos] = temp.alsoIncArrayAccess()
                defineStep(getHighlights(pos, cycleStart))
            }

            // Rotate rest of the cycle
            while (pos != cycleStart.alsoIncComparisons()) {
                pos = cycleStart

                // Find position where we put the element
                for (i in cycleStart + 1 until n) {
                    if (list[i] < item.alsoIncBoth()) {
                        pos++
                        defineStep(getHighlights(i, cycleStart, pos))
                    }
                }

                // ignore all duplicate elements
                while (item == list[pos].alsoIncBoth()) {
                    pos++
                    defineStep(getHighlights(pos, cycleStart))
                }

                // put the item to it's right position
                if (item != list[pos].alsoIncBoth()) {
                    val temp = item
                    item = list[pos].alsoIncArrayAccess()
                    list[pos] = temp.alsoIncArrayAccess()
                    defineStep(getHighlights(pos, cycleStart))
                }

                defineStep(getHighlights(pos, cycleStart))
            }
        }
        defineEnd()
    }

    private fun getHighlights(primary: Int, line: Int, secondary: Int? = null): List<Highlight> {
        val highlights = listOf(
            primary highlighted HighlightOption.COLOURED_PRIMARY,
            line highlighted HighlightOption.LINE
        )
        return if (secondary != null) {
            highlights + (secondary highlighted HighlightOption.COLOURED_SECONDARY)
        } else highlights
    }

}