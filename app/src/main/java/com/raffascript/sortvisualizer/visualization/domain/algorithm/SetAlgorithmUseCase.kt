package com.raffascript.sortvisualizer.visualization.domain.algorithm

import com.raffascript.sortvisualizer.core.data.algorithms.Algorithm
import com.raffascript.sortvisualizer.visualization.data.AlgorithmRepository
import kotlin.reflect.KClass

class SetAlgorithmUseCase(
    private val algorithmRepository: AlgorithmRepository
) {
    operator fun invoke(algorithmImpl: KClass<out Algorithm>) {
        algorithmRepository.setAlgorithm(algorithmImpl)
    }
}