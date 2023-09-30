package com.raffascript.sortvisualizer.data

import com.raffascript.sortvisualizer.data.algorithms.Algorithm
import kotlin.reflect.KClass

data class AlgorithmData(
    val id: Int,
    val name: String,
    val impl: KClass<out Algorithm>
)