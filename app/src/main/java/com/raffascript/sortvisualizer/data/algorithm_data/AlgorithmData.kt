package com.raffascript.sortvisualizer.data.algorithm_data

import com.raffascript.sortvisualizer.data.algorithms.Algorithm
import kotlin.reflect.KClass

data class AlgorithmData(
    val id: Int,
    val name: String,
    val worstCaseTimeComplexity: TimeComplexity,
    val averageCaseTimeComplexity: TimeComplexity,
    val bestCaseTimeComplexity: TimeComplexity,
    val isStable: Boolean,
    val impl: KClass<out Algorithm>
)