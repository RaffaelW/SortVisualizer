package com.raffascript.sortvisualizer.visualization.domain.algorithm

import com.raffascript.sortvisualizer.core.data.algorithms.Algorithm
import com.raffascript.sortvisualizer.visualization.data.AlgorithmRepository

class SetAlgorithmUseCase(
    private val algorithmRepository: AlgorithmRepository
) {
    operator fun invoke(algorithmConstructor: (IntArray) -> Algorithm) {
        algorithmRepository.setAlgorithm(algorithmConstructor)
    }
}