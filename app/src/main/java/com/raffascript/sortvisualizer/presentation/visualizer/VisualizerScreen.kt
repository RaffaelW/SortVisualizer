package com.raffascript.sortvisualizer.presentation.visualizer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raffascript.sortvisualizer.R
import com.raffascript.sortvisualizer.data.AlgorithmState
import com.raffascript.sortvisualizer.presentation.theme.AlgorithmsVisualizerTheme
import com.raffascript.sortvisualizer.presentation.visualizer.chart.Chart

@Composable
fun VisualizerScreen(state: VisualizerState, onEvent: (VisualizerUiEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {

        if (state.isBottomSheetShown) {
            BottomSheet(
                delay = state.sliderDelay,
                inputListSize = state.inputListSize,
                isListSizeInputValid = state.isInputListSizeValid,
                onEvent = onEvent,
                onDismiss = { onEvent(VisualizerUiEvent.HideBottomSheet) }
            )
        }

        Chart(
            list = state.sortingList,
            highlights = state.highlights,
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )

        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondary) {
            ProgressSurface(state.comparisonCount, state.arrayAccessCount)
        }

        Surface(modifier = Modifier.fillMaxWidth().weight(2f), color = MaterialTheme.colorScheme.surface) {
            AlgorithmDataSurface(state.algorithmName)
        }

        BottomBar(state.algorithmState, onEvent = onEvent)
    }
}

@Composable
fun ProgressSurface(comparisonCount: Long, arrayAccessCount: Long) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "${stringResource(R.string.num_comparisons)} $comparisonCount")
        Text(text = "${stringResource(R.string.array_accesses)} $arrayAccessCount")
    }
}

@Composable
fun AlgorithmDataSurface(algorithmName: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text(text = algorithmName, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun BottomBar(algorithmState: AlgorithmState, onEvent: (VisualizerUiEvent) -> Unit) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 10.dp,
        actions = {
            IconButton(onClick = { onEvent(VisualizerUiEvent.ShowBottomSheet) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tune),
                    contentDescription = stringResource(id = R.string.settings)
                )
            }
            IconButton(onClick = { onEvent(VisualizerUiEvent.Restart) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_replay),
                    contentDescription = stringResource(R.string.replay)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    when (algorithmState) {
                        AlgorithmState.RUNNING, AlgorithmState.RESUMED -> VisualizerUiEvent.Pause
                        AlgorithmState.READY -> VisualizerUiEvent.Play
                        AlgorithmState.PAUSED -> VisualizerUiEvent.Resume
                        AlgorithmState.FINISHED -> VisualizerUiEvent.Restart
                    }.run {
                        onEvent(this)
                    }
                }
            ) {
                val (painter, descriptionId) = when (algorithmState) {
                    AlgorithmState.READY, AlgorithmState.PAUSED -> rememberVectorPainter(Icons.Rounded.PlayArrow) to R.string.play
                    AlgorithmState.RUNNING, AlgorithmState.RESUMED -> painterResource(id = R.drawable.ic_pause) to R.string.pause
                    AlgorithmState.FINISHED -> painterResource(id = R.drawable.ic_replay) to R.string.replay
                }
                Icon(painter = painter, contentDescription = stringResource(id = descriptionId))
            }
        }
    )
}

@Preview
@Composable
fun VisualizerPreview() {
    AlgorithmsVisualizerTheme(darkTheme = true) {
        val state = VisualizerState(
            algorithmName = "BubbleSort",
            sortingList = (1..5).toList().shuffled().toIntArray(),
            algorithmState = AlgorithmState.FINISHED
        )
        VisualizerScreen(state = state, onEvent = {})
    }
}