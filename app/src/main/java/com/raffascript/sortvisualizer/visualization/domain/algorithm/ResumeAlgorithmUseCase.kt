package com.raffascript.sortvisualizer.visualization.domain.algorithm

import com.raffascript.sortvisualizer.visualization.data.AlgorithmRepository

class ResumeAlgorithmUseCase(
    private val algorithmRepository: AlgorithmRepository,
) {
    operator fun invoke() {
        algorithmRepository.resumeAlgorithm()
    }
}