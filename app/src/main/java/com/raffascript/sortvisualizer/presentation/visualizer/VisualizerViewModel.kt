package com.raffascript.sortvisualizer.presentation.visualizer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffascript.sortvisualizer.data.AlgorithmRegister
import com.raffascript.sortvisualizer.data.DelayValue
import com.raffascript.sortvisualizer.data.algorithms.Algorithm
import com.raffascript.sortvisualizer.presentation.navigation.Screen
import com.raffascript.sortvisualizer.shuffledListOfSize
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.reflect.full.primaryConstructor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class VisualizerViewModel(savedStateHandle: SavedStateHandle, algorithmRegister: AlgorithmRegister) : ViewModel() {
    companion object {
        const val DEFAULT_LIST_SIZE = 50
    }

    private val algorithmId = savedStateHandle.get<Int>(Screen.Visualizer.argAlgorithmId)!! // get arguments from navigation
    private val algorithmData = algorithmRegister.getAlgorithmById(algorithmId)!!

    private var algorithm = getAlgorithmImpl(50, 10.milliseconds)

    private val _uiState = MutableStateFlow(VisualizerState(algorithmData.name, sortingList = algorithm.getListValue()))
    val uiState = _uiState.asStateFlow()

    @OptIn(DelicateCoroutinesApi::class)
    private val algorithmThread = newSingleThreadContext("Algorithm")
    private var algorithmProgressJob = viewModelScope.launch(algorithmThread) {
        collectAlgorithmProgressFlow()
    }

    fun onEvent(event: VisualizerUiEvent) {
        when (event) {
            is VisualizerUiEvent.ShowBottomSheet -> _uiState.update { it.copy(isBottomSheetShown = true) }
            is VisualizerUiEvent.HideBottomSheet -> _uiState.update { it.copy(isBottomSheetShown = false) }
            is VisualizerUiEvent.ChangeDelay -> changeDelay(event.delay)
            is VisualizerUiEvent.ChangeListSizeInput -> changeListSizeInput(event.input)
            is VisualizerUiEvent.Play -> algorithm.start()
            is VisualizerUiEvent.Pause -> algorithm.pause()
            is VisualizerUiEvent.Resume -> algorithm.resume()
            is VisualizerUiEvent.Restart -> {
                viewModelScope.launch {
                    restartAlgorithm()
                }
            }
        }
    }

    private fun isValidListSizeInput(input: String): Boolean {
        val number = input.toIntOrNull() ?: return false
        return number in 3..5000
    }

    private fun getAlgorithmImpl(listSize: Int, delay: Duration): Algorithm {
        return algorithmData.impl.primaryConstructor!!.call(shuffledListOfSize(listSize), delay)
    }

    private suspend fun collectAlgorithmProgressFlow() {
        algorithm.getProgress().collect { progress ->
            _uiState.update {
                it.copy(
                    sortingList = progress.list,
                    algorithmState = progress.state,
                    highlights = progress.highlights,
                    arrayAccessCount = progress.arrayAccesses,
                    comparisonCount = progress.comparisons
                )
            }
        }
    }

    private suspend fun restartAlgorithm() {
        algorithmProgressJob.cancelAndJoin()
        algorithm = getAlgorithmImpl(uiState.value.listSize, uiState.value.sliderDelay.asDuration())
        algorithm.setDelay(_uiState.value.sliderDelay.asDuration())
        algorithmProgressJob = viewModelScope.launch(algorithmThread) {
            collectAlgorithmProgressFlow()
        }
    }

    private fun changeDelay(delay: DelayValue) {
        _uiState.update {
            it.copy(sliderDelay = delay)
        }
        algorithm.setDelay(delay.asDuration())
    }

    private fun changeListSizeInput(input: String) {
        val isValidInput = isValidListSizeInput(input)
        _uiState.update {
            it.copy(inputListSize = input, isInputListSizeValid = isValidInput)
        }
        if (isValidInput) {
            viewModelScope.launch {
                changeListSize(input.toInt())
            }
        }
    }

    private suspend fun changeListSize(listSize: Int) {
        _uiState.update {
            it.copy(listSize = listSize)
        }
        restartAlgorithm()
    }
}