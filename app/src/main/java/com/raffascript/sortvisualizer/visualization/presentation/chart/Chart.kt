package com.raffascript.sortvisualizer.visualization.presentation.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.raffascript.sortvisualizer.visualization.data.Highlight

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