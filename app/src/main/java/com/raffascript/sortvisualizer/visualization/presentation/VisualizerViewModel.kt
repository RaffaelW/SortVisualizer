package com.raffascript.sortvisualizer.visualization.presentation

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffascript.sortvisualizer.core.data.AlgorithmRegister
import com.raffascript.sortvisualizer.core.presentation.navigation.Screen
import com.raffascript.sortvisualizer.core.util.Resource
import com.raffascript.sortvisualizer.visualization.data.AlgorithmRepository
import com.raffascript.sortvisualizer.visualization.data.AlgorithmState
import com.raffascript.sortvisualizer.visualization.data.DelayValue
import com.raffascript.sortvisualizer.visualization.domain.ChangeDelayUseCase
import com.raffascript.sortvisualizer.visualization.domain.ChangeListSizeUseCase
import com.raffascript.sortvisualizer.visualization.domain.LoadUserPreferencesUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class VisualizerViewModel(
    savedStateHandle: SavedStateHandle,
    algorithmRegister: AlgorithmRegister,
    private val algorithmRepository: AlgorithmRepository,
    loadUserPreferencesUseCase: LoadUserPreferencesUseCase,
    private val changeListSizeUseCase: ChangeListSizeUseCase,
    private val changeDelayUseCase: ChangeDelayUseCase
) : ViewModel(), DefaultLifecycleObserver {

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
        viewModelScope.launch(algorithmThread) {
            collectAlgorithmProgressFlow()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        algorithmRepository.pauseAlgorithm()
        Log.d("VisualizerViewModel", "onPause: running")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        algorithmRepository.pauseAlgorithm()
        Log.d("VisualizerViewModel", "onStop: running")
    }

    fun onEvent(event: VisualizerUiEvent) {
        when (event) {
            is VisualizerUiEvent.ShowBottomSheet -> _uiState.update { it.copy(showBottomSheet = true) }
            is VisualizerUiEvent.HideBottomSheet -> _uiState.update { it.copy(showBottomSheet = false) }
            is VisualizerUiEvent.ChangeDelay -> changeDelay(event.delay)
            is VisualizerUiEvent.ChangeListSizeInput -> changeListSizeInput(event.input)
            is VisualizerUiEvent.Restart -> algorithmRepository.restartAlgorithm()
            is VisualizerUiEvent.ToggleAlgorithmState -> {
                when (_uiState.value.algorithmState) {
                    AlgorithmState.RUNNING -> algorithmRepository.pauseAlgorithm()
                    AlgorithmState.READY, AlgorithmState.UNINITIALIZED -> algorithmRepository.startAlgorithm()
                    AlgorithmState.PAUSED -> algorithmRepository.resumeAlgorithm()
                    AlgorithmState.FINISHED -> algorithmRepository.restartAlgorithm()
                }
            }
        }
    }

    private suspend fun collectAlgorithmProgressFlow() {
        algorithmRepository.getProgressFlow(algorithmData.constructor).collect { progress ->
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