package com.raffascript.sortvisualizer.visualization.data

interface AlgorithmProgressHandler {

    suspend fun onProgressChanged(vararg highlights: Highlight)

    suspend fun onFinish()
}