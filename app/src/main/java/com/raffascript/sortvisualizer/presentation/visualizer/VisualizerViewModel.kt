package com.raffascript.sortvisualizer.presentation.visualizer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raffascript.sortvisualizer.convert
import com.raffascript.sortvisualizer.data.algorithm_data.AlgorithmRegister
import com.raffascript.sortvisualizer.data.algorithms.Algorithm
import com.raffascript.sortvisualizer.data.preferences.UserPreferencesDataSource
import com.raffascript.sortvisualizer.data.preferences.UserPreferencesRepository
import com.raffascript.sortvisualizer.data.viszualization.DelayValue
import com.raffascript.sortvisualizer.data.viszualization.HighlightOption
import com.raffascript.sortvisualizer.device.ServiceProvider
import com.raffascript.sortvisualizer.device.SoundPlayer
import com.raffascript.sortvisualizer.domain.CheckListSizeInputUseCase
import com.raffascript.sortvisualizer.presentation.navigation.Screen
import com.raffascript.sortvisualizer.shuffledListOfSize
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.reflect.full.primaryConstructor
import kotlin.time.Duration

class VisualizerViewModel(
    savedStateHandle: SavedStateHandle,
    algorithmRegister: AlgorithmRegister,
    private val checkListSizeInputUseCase: CheckListSizeInputUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val algorithmId = savedStateHandle.get<Int>(Screen.Visualizer.argAlgorithmId)!! // get arguments from navigation
    private val algorithmData = algorithmRegister.getAlgorithmById(algorithmId)!!

    private var algorithm = getAlgorithmImpl(UserPreferencesDataSource.DEFAULT_LIST_SIZE, DelayValue.default.asDuration())

    private val userPreferences = userPreferencesRepository.getUserPreferencesFlow()

    private val _uiState = MutableStateFlow(VisualizerState(algorithmData, sortingList = algorithm.getListValue()))
    val uiState = combine(_uiState, userPreferences) { state, userPreferences ->
        if (soundPlayer.soundDuration != userPreferences.delay.asDuration()) {
            soundPlayer = ServiceProvider.getSoundPlayer(userPreferences.delay.asDuration())
            soundPlayer.start()
        }
        return@combine state.copy(
            sliderDelay = userPreferences.delay,
            listSize = userPreferences.listSize,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _uiState.asStateFlow().value)

    private var soundPlayer: SoundPlayer = ServiceProvider.getSoundPlayer(uiState.value.sliderDelay.asDuration()).also {
        it.start()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private val algorithmThread = newSingleThreadContext("Algorithm")
    private var algorithmProgressJob = viewModelScope.launch(algorithmThread) {
        collectAlgorithmProgressFlow()
    }

    init {
        viewModelScope.launch {
            userPreferences.collect {
                algorithm.setDelay(it.delay.asDuration())
                if (algorithm.listSize != it.listSize) {
                    restartAlgorithm()
                }
            }
        }
    }

    fun onEvent(event: VisualizerUiEvent) {
        when (event) {
            is VisualizerUiEvent.ShowBottomSheet -> _uiState.update { it.copy(showBottomSheet = true) }
            is VisualizerUiEvent.HideBottomSheet -> _uiState.update { it.copy(showBottomSheet = false) }
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

    private fun getAlgorithmImpl(listSize: Int, delay: Duration): Algorithm {
        return algorithmData.impl.primaryConstructor!!.call(shuffledListOfSize(listSize), delay)
    }

    private suspend fun collectAlgorithmProgressFlow() {
        algorithm.getProgressFlow().collect { progress ->
            _uiState.update {
                it.copy(
                    sortingList = progress.list,
                    algorithmState = progress.state,
                    highlights = progress.highlights,
                    arrayAccessCount = progress.arrayAccesses,
                    comparisonCount = progress.comparisons
                )
            }

            var index = progress.highlights.find { it.highlightOption == HighlightOption.COLOURED_PRIMARY }?.index
            if (index != null) {
                index = if (index > progress.list.lastIndex) progress.list.lastIndex else if (index < 0) 0 else index
                val frequency = progress.list.indices.convert(progress.list[index], 200..10000)
                soundPlayer.play(frequency)
            }
        }
    }

    private suspend fun restartAlgorithm() {
        algorithmProgressJob.cancelAndJoin()
        algorithm = getAlgorithmImpl(uiState.value.listSize, uiState.value.sliderDelay.asDuration())
        algorithm.setDelay(uiState.value.sliderDelay.asDuration())
        algorithmProgressJob = viewModelScope.launch(algorithmThread) {
            collectAlgorithmProgressFlow()
        }
    }

    private fun changeDelay(delay: DelayValue) {
        _uiState.update {
            it.copy(sliderDelay = delay)
        }
        viewModelScope.launch {
            userPreferencesRepository.saveDelay(delay)
        }
    }

    private fun changeListSizeInput(input: String) {
        val isValidInput = checkListSizeInputUseCase(input)
        _uiState.update {
            it.copy(isInputListSizeValid = isValidInput)
        }
        if (isValidInput) {
            viewModelScope.launch {
                userPreferencesRepository.saveListSize(input.toInt())
            }
        }
    }

}