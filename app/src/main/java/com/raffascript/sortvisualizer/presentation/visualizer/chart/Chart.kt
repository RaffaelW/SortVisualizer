package com.raffascript.sortvisualizer.presentation.visualizer.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.raffascript.sortvisualizer.data.viszualization.Highlight

@Composable
fun Chart(list: IntArray, highlights: List<Highlight>, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = {
            ChartSurfaceView(it)
        },
        update = {
            it.updateData(list, highlights)
        }
    )
}