package com.raffascript.sortvisualizer.presentation.visualizer.chart

/*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ChartComposable(list: List<Int>, currentIndex: Int?, savedIndices: List<Int>, modifier: Modifier = Modifier) {
    val localDensity = LocalDensity.current
    var height by remember { mutableStateOf(0.dp) }
    Row(
        modifier = modifier.background(Color.Black).onGloballyPositioned {
            height = with(localDensity) { it.size.height.toDp() }
        }, verticalAlignment = Alignment.Bottom
    ) {
        list.forEachIndexed { index, value ->
            Bar(chartHeight = height, value = value, maxValue = list.size, currentIndex == index)
            if (savedIndices.contains(index)) {
                Line(color = Color.Green)
            }
        }
    }
}

@Composable
fun RowScope.Bar(chartHeight: Dp, value: Int, maxValue: Int, highlight: Boolean) {
    val height by remember(chartHeight, value, maxValue) {
        var calculatedHeight = (value / maxValue.toFloat()) * chartHeight.value
        if (calculatedHeight < 5) {
            calculatedHeight -= 1
        }
        mutableStateOf(calculatedHeight.dp)
    }
    val color = if (highlight) Color.Red else Color.White
    Box(
        modifier = Modifier
            .weight(1f)
            .height(height)
            .background(color)
    )
}

@Composable
fun Line(color: Color) {
    Divider(modifier = Modifier.width(1.dp).fillMaxHeight(), color = color)
}*/