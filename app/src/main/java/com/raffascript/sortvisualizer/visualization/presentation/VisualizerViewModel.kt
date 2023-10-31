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
    private val changeDelayUseCase: ChangeDelayUseCase
) : ViewModel() {

    private val algorithmId = savedStateHandle.get<Int>(Screen.Visualizer.argAlgorithmId)!! // get arguments from navigation
    private val algorithmData = algorithmRegister.getAlgorithmById(algorithmId)!!

    private val userPreferences = loadUserPreferencesUseCase()

    private val _uiState = MutableStateFlow(VisualizerState(algorithmData))
    val uiState = combine(_uiState, userPreferences) { state, userPreferences ->
/*
        if (soundPlayer.soundDuration != userPreferences.delay.asDuration()) {
            soundPlayer = ServiceProvider.getSoundPlayer(userPreferences.delay.asDuration())
            soundPlayer.start()
        }
*/
        return@combine state.copy(
            sliderDelay = userPreferences.delay,
            listSize = userPreferences.listSize,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _uiState.asStateFlow().value)

/*
    private var soundPlayer: SoundPlayer = ServiceProvider.getSoundPlayer(uiState.value.sliderDelay.asDuration()).also {
        it.start()
    }
*/

    @OptIn(DelicateCoroutinesApi::class)
    private val algorithmThread = newSingleThreadContext("Algorithm")

    init {
        setAlgorithmUseCase(algorithmData.impl)
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

            /*var index = progress.highlights.find { it.highlightOption == HighlightOption.COLOURED_PRIMARY }?.index
            if (index != null) {
                index = if (index > progress.list.lastIndex) progress.list.lastIndex else if (index < 0) 0 else index
                val frequency = progress.list.indices.convert(progress.list[index], 200..10000)
                soundPlayer.play(frequency)
            }

            if (progress.state == AlgorithmState.FINISHED) {
                soundPlayer.stop()
            }*/
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