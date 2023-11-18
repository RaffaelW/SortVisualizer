package com.raffascript.sortvisualizer.visualization.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffascript.sortvisualizer.core.data.AlgorithmRegister
import com.raffascript.sortvisualizer.core.presentation.navigation.Screen
import com.raffascript.sortvisualizer.core.util.Resource
import com.raffascript.sortvisualizer.visualization.data.DelayValue
import com.raffascript.sortvisualizer.visualization.domain.*
import com.raffascript.sortvisualizer.visualization.domain.algorithm.GetAlgorithmProgressFlowUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.PauseAlgorithmUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.RestartAlgorithmUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.ResumeAlgorithmUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.SetAlgorithmUseCase
import com.raffascript.sortvisualizer.visualization.domain.algorithm.StartAlgorithmUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class VisualizerViewModel(
    savedStateHandle: SavedStateHandle,
    algorithmRegister: AlgorithmRegister,
    loadUserPreferencesUseCase: LoadUserPreferencesUseCase,
    setAlgorithmUseCase: SetAlgorithmUseCase,
    private val startAlgorithmUseCase: StartAlgorithmUseCase,
    private val pauseAlgorithmUseCase: PauseAlgorithmUseCase,
    private val resumeAlgorithmUseCase: ResumeAlgorithmUseCase,
    private val restartAlgorithmUseCase: RestartAlgorithmUseCase,
    private val getAlgorithmProgressFlowUseCase: GetAlgorithmProgressFlowUseCase,
    private val changeListSizeUseCase: ChangeListSizeUseCase,
    private val changeDelayUseCase: ChangeDelayUseCase,
) : ViewModel() {

    private val algorithmId = savedStateHandle.get<Int>(Screen.Visualizer.argAlgorithmId)!! // get arguments from navigation
    private val algorithmData = algorithmRegister.getAlgorithmById(algorithmId)!!

    private val userPreferences = loadUserPreferencesUseCase()

    private val _uiState = MutableStateFlow(VisualizerState(algorithmData))
    val uiState = combine(_uiState, userPreferences) { state, userPreferences ->
        return@combine state.copy(
            sliderDelay = userPreferences.delay,
            listSize = userPreferences.listSize
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _uiState.asStateFlow().value)

    @OptIn(DelicateCoroutinesApi::class)
    private val algorithmThread = newSingleThreadContext("Algorithm")

    init {
        setAlgorithmUseCase(algorithmData.constructor)
        viewModelScope.launch(algorithmThread) {
            collectAlgorithmProgressFlow()
        }
    }

    fun onEvent(event: VisualizerUiEvent) {
        when (event) {
            is VisualizerUiEvent.ShowBottomSheet -> _uiState.update { it.copy(showBottomSheet = true) }
            is VisualizerUiEvent.HideBottomSheet -> _uiState.update { it.copy(showBottomSheet = false) }
            is VisualizerUiEvent.ChangeDelay -> changeDelay(event.delay)
            is VisualizerUiEvent.ChangeListSizeInput -> changeListSizeInput(event.input)
            is VisualizerUiEvent.Play -> startAlgorithmUseCase()
            is VisualizerUiEvent.Pause -> pauseAlgorithmUseCase()
            is VisualizerUiEvent.Resume -> resumeAlgorithmUseCase()
            is VisualizerUiEvent.Restart -> restartAlgorithmUseCase()
        }
    }

    private suspend fun collectAlgorithmProgressFlow() {
        getAlgorithmProgressFlowUseCase().collect { progress ->
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

    private fun changeDelay(delay: DelayValue) {
        _uiState.update {
            it.copy(sliderDelay = delay)
        }
        viewModelScope.launch {
            changeDelayUseCase(delay)
        }
    }

    private fun changeListSizeInput(input: String) {
        viewModelScope.launch {
            val isSuccess = changeListSizeUseCase(input) is Resource.Success
            _uiState.update {
                it.copy(isInputListSizeValid = isSuccess)
            }
        }
    }

}