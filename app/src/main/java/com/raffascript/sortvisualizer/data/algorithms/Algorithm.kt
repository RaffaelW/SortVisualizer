package com.raffascript.sortvisualizer.data.algorithms

import com.raffascript.sortvisualizer.data.AlgorithmState
import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgress
import com.raffascript.sortvisualizer.data.viszualization.AlgorithmProgressHandler
import com.raffascript.sortvisualizer.data.viszualization.Highlight
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

abstract class Algorithm(protected var list: IntArray, delay: Duration) {

    private var state = AlgorithmState.READY

    private var delayMillis = delay.inWholeMilliseconds
    private var isDelayNotZero = delayMillis != 0L

    private var progress = AlgorithmProgress(list, state, emptyList(), 0, 0)

    protected var arrayAccesses = 0L
    protected var comparisons = 0L

    fun getListValue() = list.clone()

    suspend fun getProgress(): Flow<AlgorithmProgress> = flow {
        state = AlgorithmState.READY
        emit(getUpdatedProgress(list, state, emptyList()))

        waitUntilStateIs(AlgorithmState.RUNNING)
        emit(getUpdatedProgress(state = state))

        sort(object : AlgorithmProgressHandler {
            override suspend fun onProgressChanged(vararg highlights: Highlight) {
                currentCoroutineContext().ensureActive()

                @Suppress("NAME_SHADOWING") val highlights = highlights.toList()

                if (state == AlgorithmState.PAUSED) {
                    emit(getUpdatedProgress(state = state))
                    waitUntilStateIs(AlgorithmState.RESUMED)
                    emit(getUpdatedProgress(list, state, highlights))
                }
                emit(getUpdatedProgress(list = list, highlights = highlights))
                if (isDelayNotZero) {
                    wait(delayMillis)
                }
            }

            override suspend fun onFinish() {
                state = AlgorithmState.FINISHED
                emit(getUpdatedProgress(list, state, emptyList()))
            }
        })
    }

    fun start() {
        state = AlgorithmState.RUNNING
    }

    fun pause() {
        state = AlgorithmState.PAUSED
    }

    fun resume() {
        state = AlgorithmState.RESUMED
    }

    fun setDelay(duration: Duration) {
        delayMillis = duration.inWholeMilliseconds
        isDelayNotZero = delayMillis != 0L
    }

    protected abstract suspend fun FlowCollector<AlgorithmProgress>.sort(progressHandler: AlgorithmProgressHandler)

    private fun getUpdatedProgress(
        list: IntArray = progress.list,
        state: AlgorithmState = progress.state,
        highlights: List<Highlight> = progress.highlights,
    ): AlgorithmProgress {
        progress = progress.copy(
            list = list,
            state = state,
            highlights = highlights,
            arrayAccesses = arrayAccesses,
            comparisons = comparisons
        )
        return progress
    }

    private fun wait(millis: Long) {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < millis) {
//            yield()
        }
    }

    private suspend fun waitUntilStateIs(algorithmState: AlgorithmState) {
        while (state != algorithmState) {
            yield() // wait
        }
    }
}