package com.raffascript.sortvisualizer.selection.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raffascript.sortvisualizer.core.data.AlgorithmData
import com.raffascript.sortvisualizer.core.data.TimeComplexity
import com.raffascript.sortvisualizer.core.data.algorithms.InsertionSort
import com.raffascript.sortvisualizer.core.presentation.theme.AlgorithmsVisualizerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectionScreen(state: SelectionState, navigateToVisualizer: (Int) -> Unit) {
    val activity = LocalContext.current as Activity
    LaunchedEffect(key1 = Unit) {
        @SuppressLint("SourceLockedOrientationActivity")
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        state.algorithmData.toList().forEach { (key, list) ->

            stickyHeader {
                Header(stringResource(id = key.fullName))
            }

            itemsIndexed(items = list, key = { _, item -> item.id }) { index, item ->
                Row(item, onClick = { navigateToVisualizer(item.id) })
                if (index < list.lastIndex) {
                    Divider(color = MaterialTheme.colorScheme.onBackground, thickness = Dp.Hairline)
                }

            }

        }
    }
}

@Composable
fun Header(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun Row(algorithm: AlgorithmData, onClick: () -> Unit) {
    Text(
        text = algorithm.name,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SelectionScreenPreview() {
    AlgorithmsVisualizerTheme(darkTheme = false) {
        val state = SelectionState(
            listOf(
                AlgorithmData(
                    0,
                    "InsertionSort",
                    TimeComplexity.QUADRATIC,
                    TimeComplexity.QUADRATIC,
                    TimeComplexity.LINEAR,
                    true,
                    ::InsertionSort
                )
            ).groupBy { it.averageCaseTimeComplexity }
        )
        SelectionScreen(state = state, navigateToVisualizer = {})
    }
}