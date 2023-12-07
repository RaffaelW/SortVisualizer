package com.raffascript.sortvisualizer.core.data

import com.raffascript.sortvisualizer.core.data.algorithms.Algorithm

data class AlgorithmData(
    val id: Int,
    val name: String,
    val group: AlgorithmGroup,
    val worstCaseTimeComplexity: TimeComplexity,
    val averageCaseTimeComplexity: TimeComplexity,
    val bestCaseTimeComplexity: TimeComplexity,
    val isStable: Boolean,
    val constructor: (IntArray) -> Algorithm,
)