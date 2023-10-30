package com.raffascript.sortvisualizer.visualization.data

interface AlgorithmProgressHandler {

    suspend fun onStep(vararg highlights: Highlight)

    suspend fun onFinish()
}