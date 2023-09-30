package com.raffascript.sortvisualizer.presentation.selection

import androidx.lifecycle.ViewModel
import com.raffascript.sortvisualizer.data.AlgorithmRegister
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectionViewModel(algorithmRegister: AlgorithmRegister) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectionState(algorithmRegister.getAllAlgorithms()))
    val uiState = _uiState.asStateFlow()
}