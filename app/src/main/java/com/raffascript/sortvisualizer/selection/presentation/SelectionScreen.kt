package com.raffascript.sortvisualizer.selection.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raffascript.sortvisualizer.R
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

    Column {

        Text(
            text = "Wähle einen Sortieralgorithmus",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 22.sp,
            modifier = Modifier.fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 8.dp, vertical = 12.dp)
        )

        val isExpandedMap = remember {
            List(state.algorithmData.size) { index ->
                index to true
            }.toMutableStateMap()
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            state.algorithmData.toList().forEachIndexed { index, (key, list) ->

                stickyHeader {
                    Header(
                        text = stringResource(id = key.fullName),
                        isExpanded = isExpandedMap[index] ?: true,
                        onClick = {
                            isExpandedMap[index] = !(isExpandedMap[index] ?: true)
                        }
                    )
                }

                items(items = list, key = { item -> item.id }) { item ->
                    AnimatedVisibility(isExpandedMap[index] ?: true) {
                        ListRow(item, onClick = { navigateToVisualizer(item.id) })
                        Divider(thickness = Dp.Hairline)
                    }
                }

            }
        }
    }
}

@Composable
fun Header(text: String, isExpanded: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).weight(1f)
            )

            val (icon, descriptionRes) = if (isExpanded) {
                Pair(Icons.Rounded.KeyboardArrowDown, R.string.fold)
            } else {
                Pair(Icons.Rounded.KeyboardArrowUp, R.string.expand)
            }
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = descriptionRes),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterVertically).padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun ListRow(algorithm: AlgorithmData, onClick: () -> Unit) {
    Text(
        text = algorithm.name,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 36.dp, vertical = 8.dp)
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