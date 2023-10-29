package com.raffascript.sortvisualizer.selection.presentation

import androidx.lifecycle.ViewModel
import com.raffascript.sortvisualizer.core.data.AlgorithmRegister
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectionViewModel(algorithmRegister: AlgorithmRegister) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectionState(algorithmRegister.getAllAlgorithms()))
    val uiState = _uiState.asStateFlow()
}