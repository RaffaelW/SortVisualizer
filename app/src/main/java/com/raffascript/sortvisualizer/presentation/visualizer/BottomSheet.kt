package com.raffascript.sortvisualizer.presentation.visualizer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raffascript.sortvisualizer.R
import com.raffascript.sortvisualizer.data.DelayValue
import com.raffascript.sortvisualizer.data.preferences.UserPreferencesDataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    delay: DelayValue,
    inputListSize: String,
    isListSizeInputValid: Boolean,
    onEvent: (VisualizerUiEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.delay), style = MaterialTheme.typography.labelMedium)
            Row(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    modifier = Modifier.weight(1f),
                    value = delay.ordinal.toFloat(),
                    onValueChange = {
                        onEvent(VisualizerUiEvent.ChangeDelay(DelayValue.values()[it.toInt()]))
                    },
                    valueRange = 0f..DelayValue.values().lastIndex.toFloat(),
                    steps = DelayValue.values().size - 2
                )
                Text(
                    text = "${delay.millis} ms",
                    modifier = Modifier.width(80.dp).align(Alignment.CenterVertically),
                    textAlign = TextAlign.End
                )
            }

            Text(
                text = stringResource(id = R.string.size_of_list),
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                style = MaterialTheme.typography.labelMedium
            )
            TextField(
                value = inputListSize,
                onValueChange = { onEvent(VisualizerUiEvent.ChangeListSizeInput(it)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onEvent(VisualizerUiEvent.HideBottomSheet) }),
                singleLine = true,
                isError = !isListSizeInputValid
            )

            Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onEvent(VisualizerUiEvent.HideBottomSheet) }
                ) {
                    Text(
                        text = stringResource(R.string.finish),
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(
                    onClick = {
                        onEvent(VisualizerUiEvent.ChangeDelay(DelayValue.default))
                        onEvent(VisualizerUiEvent.ChangeListSizeInput("${UserPreferencesDataStore.DEFAULT_LIST_SIZE}"))
                    }
                ) {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = stringResource(R.string.reset))
                }
            }

            Spacer(modifier = Modifier.size(50.dp))
        }
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    BottomSheet(DelayValue.DELAY_100, "50", true, {}, {})
}