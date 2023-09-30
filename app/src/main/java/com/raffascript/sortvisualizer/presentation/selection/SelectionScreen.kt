package com.raffascript.sortvisualizer.presentation.selection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raffascript.sortvisualizer.data.AlgorithmData
import com.raffascript.sortvisualizer.data.algorithms.InsertionSort
import com.raffascript.sortvisualizer.presentation.theme.AlgorithmsVisualizerTheme

@Composable
fun SelectionScreen(state: SelectionState, navigateToVisualizer: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(items = state.algorithmData, key = { _, item -> item.id }) { index, item ->
            Row(item, onClick = { navigateToVisualizer(item.id) })
            if (index < state.algorithmData.lastIndex) {
                Divider(color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@Composable
fun Row(algorithm: AlgorithmData, onClick: () -> Unit) {
    Text(
        text = algorithm.name,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun SelectionScreenPreview() {
    AlgorithmsVisualizerTheme(darkTheme = false) {
        val state = SelectionState(
            listOf(
                AlgorithmData(1, "InsertionSort", InsertionSort::class),
                AlgorithmData(2, "BubbleSort", InsertionSort::class),
                AlgorithmData(3, "QuickSort", InsertionSort::class),
                AlgorithmData(4, "RadixSort", InsertionSort::class),
            )
        )
        SelectionScreen(state = state, navigateToVisualizer = {})
    }
}