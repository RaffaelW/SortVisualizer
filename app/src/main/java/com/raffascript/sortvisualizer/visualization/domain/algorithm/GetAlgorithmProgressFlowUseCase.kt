package com.raffascript.sortvisualizer.visualization.domain.algorithm

import com.raffascript.sortvisualizer.core.data.algorithms.Algorithm
import com.raffascript.sortvisualizer.visualization.data.AlgorithmProgress
import com.raffascript.sortvisualizer.visualization.data.AlgorithmRepository
import kotlinx.coroutines.flow.Flow

class GetAlgorithmProgressFlowUseCase(
    private val algorithmRepository: AlgorithmRepository
) {
    suspend operator fun invoke(algorithmConstructor: (IntArray) -> Algorithm): Flow<AlgorithmProgress> {
        return algorithmRepository.getProgressFlow(algorithmConstructor)
    }
}