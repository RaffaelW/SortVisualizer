package com.raffascript.sortvisualizer.data.viszualization

interface AlgorithmProgressHandler {

    suspend fun onProgressChanged(vararg highlights: Highlight)

    suspend fun onFinish()
}