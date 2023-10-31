package com.raffascript.sortvisualizer.visualization.presentation

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raffascript.sortvisualizer.R
import com.raffascript.sortvisualizer.core.data.AlgorithmData
import com.raffascript.sortvisualizer.core.data.TimeComplexity
import com.raffascript.sortvisualizer.core.data.algorithms.BubbleSort
import com.raffascript.sortvisualizer.core.presentation.MainActivity
import com.raffascript.sortvisualizer.core.presentation.theme.AlgorithmsVisualizerTheme
import com.raffascript.sortvisualizer.visualization.data.AlgorithmState
import com.raffascript.sortvisualizer.visualization.presentation.chart.Chart

@Composable
fun VisualizerScreen(state: VisualizerState, onEvent: (VisualizerUiEvent) -> Unit) {
    val configuration = LocalConfiguration.current

    if (state.showBottomSheet) {
        BottomSheet(
            delay = state.sliderDelay,
            listSize = state.listSize,
            isListSizeInputValid = state.isInputListSizeValid,
            onEvent = onEvent,
            onDismiss = { onEvent(VisualizerUiEvent.HideBottomSheet) }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                ContentLandscape(state = state, onEvent = onEvent)
            }
            else -> {
                ContentPortrait(state = state, onEvent = onEvent)
            }
        }
    }
}

@Composable
fun ColumnScope.ContentPortrait(state: VisualizerState, onEvent: (VisualizerUiEvent) -> Unit) {
    Chart(
        list = state.sortingList,
        highlights = state.highlights,
        modifier = Modifier.fillMaxWidth().height(300.dp)
    )

    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondary) {
        ProgressSurface(state.comparisonCount, state.arrayAccessCount)
    }

    Surface(modifier = Modifier.fillMaxWidth().weight(1f), color = MaterialTheme.colorScheme.surface) {
        AlgorithmDataSurface(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            algorithmData = state.algorithmData
        )
    }

    BottomBar(state.algorithmData.name, false, state.algorithmState, state.isSoundOn, onEvent = onEvent)
}

@Composable
fun ColumnScope.ContentLandscape(state: VisualizerState, onEvent: (VisualizerUiEvent) -> Unit) {
    // use LazyColumn to get access to Modifier.fillParentMaxHeight()
    LazyColumn(modifier = Modifier.weight(1f)) {
        item {
            Chart(
                list = state.sortingList,
                highlights = state.highlights,
                modifier = Modifier.fillMaxWidth().fillParentMaxHeight()
            )

            Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondary) {
                ProgressSurface(state.comparisonCount, state.arrayAccessCount)
            }

            Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surface) {
                AlgorithmDataSurface(algorithmData = state.algorithmData)
            }
        }
    }

    BottomBar(state.algorithmData.name, true, state.algorithmState, state.isSoundOn, onEvent = onEvent)
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
fun AlgorithmDataSurface(modifier: Modifier = Modifier, algorithmData: AlgorithmData) {
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = algorithmData.name, style = MaterialTheme.typography.titleLarge)

        val isStableStringRes = if (algorithmData.isStable) R.string.yes else R.string.no
        val strings = listOf(
            R.string.worst_case_time_complexity to algorithmData.worstCaseTimeComplexity.abbreviation,
            R.string.average_time_complexity to algorithmData.averageCaseTimeComplexity.abbreviation,
            R.string.best_case_time_complexity to algorithmData.bestCaseTimeComplexity.abbreviation,
            R.string.is_stable to stringResource(id = isStableStringRes)
        )
        strings.forEach { (titleResId, value) ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = titleResId),
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = value, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun BottomBar(
    algorithmName: String,
    showName: Boolean,
    algorithmState: AlgorithmState,
    isSoundOn: Boolean,
    onEvent: (VisualizerUiEvent) -> Unit
) {
    val activity = LocalContext.current as Activity
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
            IconButton(onClick = {
                activity.requestedOrientation = if (MainActivity.isOrientationLandscape) {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
            }) {
                val (drawableRes, descriptionRes) = if (MainActivity.isOrientationLandscape) {
                    Pair(R.drawable.ic_fullscreen_exit, R.string.fullscreen_exit)
                } else {
                    Pair(R.drawable.ic_fullscreen, R.string.fullscreen)
                }
                Icon(
                    painter = painterResource(id = drawableRes),
                    contentDescription = stringResource(descriptionRes)
                )
            }
            IconButton(onClick = {
                val event = if (isSoundOn) VisualizerUiEvent.TurnSoundOff else VisualizerUiEvent.TurnSoundOn
                onEvent(event)
            }) {
                val (drawableRes, contentDescriptionRes) = if (isSoundOn) {
                    R.drawable.ic_volume_off to R.string.turn_sound_off
                } else {
                    R.drawable.ic_volume_up to R.string.turn_sound_on
                }
                Icon(
                    painter = painterResource(drawableRes),
                    contentDescription = stringResource(contentDescriptionRes)
                )
            }

            if (showName) {
                Text(
                    text = algorithmName,
                    modifier = Modifier.padding(horizontal = 8.dp).weight(2f),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    when (algorithmState) {
                        AlgorithmState.RUNNING -> VisualizerUiEvent.Pause
                        AlgorithmState.READY, AlgorithmState.UNINITIALIZED -> VisualizerUiEvent.Play
                        AlgorithmState.PAUSED -> VisualizerUiEvent.Resume
                        AlgorithmState.FINISHED -> VisualizerUiEvent.Restart
                    }.run {
                        onEvent(this)
                    }
                }
            ) {
                val (painter, descriptionId) = when (algorithmState) {
                    AlgorithmState.RUNNING -> painterResource(id = R.drawable.ic_pause) to R.string.pause
                    AlgorithmState.FINISHED -> painterResource(id = R.drawable.ic_replay) to R.string.replay
                    else -> rememberVectorPainter(Icons.Rounded.PlayArrow) to R.string.play
                }
                Icon(painter = painter, contentDescription = stringResource(id = descriptionId))
            }
        }
    )
}

@Preview(name = "Portrait")
@Preview(name = "Landscape", device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun VisualizerPreview() {
    AlgorithmsVisualizerTheme(darkTheme = true) {
        val state = VisualizerState(
            algorithmData = AlgorithmData(
                0,
                "BubbleSort",
                TimeComplexity.QUASILINEAR,
                TimeComplexity.QUADRATIC,
                TimeComplexity.INFINITE,
                true,
                BubbleSort::class
            ),
            sortingList = (1..5).toList().shuffled().toIntArray(),
            algorithmState = AlgorithmState.FINISHED
        )
        VisualizerScreen(state = state, onEvent = {})
    }
}