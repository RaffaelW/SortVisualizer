package com.raffascript.sortvisualizer.visualization.data

import android.util.Log
import com.raffascript.sortvisualizer.core.data.algorithms.Algorithm
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class AlgorithmRepository(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    private lateinit var algorithm: Algorithm
    private var state = AlgorithmState.UNINITIALIZED
    private var delayMillis = 0
    private var listSize = 0

    private var isRestartRequested = false

    private lateinit var progress: AlgorithmProgress

    init {
        CoroutineScope(Dispatchers.Default).launch {
            userPreferencesRepository.getUserPreferencesFlow().collect { preferences ->
                delayMillis = preferences.delay.millis
                if (listSize != preferences.listSize && ::algorithm.isInitialized) {
                    listSize = preferences.listSize
                    isRestartRequested = true
                } else {
                    listSize = preferences.listSize
                }
            }
        }
    }

    fun startAlgorithm(): Boolean {
        return if (state == AlgorithmState.READY) {
            state = AlgorithmState.RUNNING
            true
        } else {
            false
        }
    }

    fun pauseAlgorithm(): Boolean {
        return if (state == AlgorithmState.RUNNING) {
            state = AlgorithmState.PAUSED
            return true
        } else {
            false
        }
    }

    fun resumeAlgorithm(): Boolean {
        return if (state == AlgorithmState.PAUSED) {
            state = AlgorithmState.RUNNING
            true
        } else {
            false
        }
    }

    fun restartAlgorithm() {
        isRestartRequested = true
    }

    private fun getUpdatedProgress(
        list: IntArray = progress.list,
        state: AlgorithmState = progress.state,
        highlights: List<Highlight> = progress.highlights,
        arrayAccesses: Long = progress.arrayAccesses,
        comparisons: Long = progress.comparisons
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

    suspend fun getProgressFlow(algorithmConstructor: (IntArray) -> Algorithm): Flow<AlgorithmProgress> = flow {

        fun setAlgorithm() {
            val array = generateShuffledArray(listSize)
            algorithm = algorithmConstructor(array)
            state = AlgorithmState.READY
            isRestartRequested = false
            progress = AlgorithmProgress(array, state, emptyList(), 0, 0)
        }

        setAlgorithm()
        while (true) {
            try {
                handleStateCycle()
            } catch (e: AlgorithmCancellationException) {
                isRestartRequested = false
                setAlgorithm()
            }
        }
    }

    private suspend fun FlowCollector<AlgorithmProgress>.handleStateCycle() {
        emit(getUpdatedProgress(state = state))

        waitForState(AlgorithmState.RUNNING)
        emit(getUpdatedProgress(state = state))

        val onStep: suspend (IntArray, List<Highlight>, Long, Long) -> Unit =
            { list, highlights, arrayAccesses, comparisons ->
                currentCoroutineContext().ensureActive()

                emit(getUpdatedProgress(list, state, highlights, arrayAccesses, comparisons))

                wait(delayMillis)

                if (state == AlgorithmState.PAUSED) {
                    Log.d("SortVisualizer", "handleStateCycle: PAUSING ###########################")
                    emit(getUpdatedProgress(state = state))
                    waitForState(AlgorithmState.RUNNING)
                    Log.d("SortVisualizer", "handleStateCycle: RUNNING AGAIN ###########################")
                    emit(getUpdatedProgress(state = state))
                }

                if (isRestartRequested) {
                    throw AlgorithmCancellationException()
                }
            }

        val onFinish: suspend (IntArray, Long, Long) -> Unit = { list, arrayAccesses, comparisons ->
            state = AlgorithmState.FINISHED
            emit(
                getUpdatedProgress(
                    list = list,
                    state = state,
                    highlights = emptyList(),
                    arrayAccesses = arrayAccesses,
                    comparisons = comparisons
                )
            )
        }

        algorithm.startSorting(onStep, onFinish)

        waitForState(AlgorithmState.READY)
    }

    private fun generateShuffledArray(size: Int): IntArray {
        return IntArray(size).apply {
            forEachIndexed { index, _ ->
                this[index] = index + 1
            }
            shuffle()
        }
    }

    private suspend fun FlowCollector<AlgorithmProgress>.waitForState(requestedState: AlgorithmState) {
        val startProgress = progress.copy()
        while (state != requestedState) {
            yield() // wait
            if (startProgress.shouldEmitNext(progress)) {
                emit(progress)
            }
            if (isRestartRequested) {
                throw AlgorithmCancellationException()
            }
        }
    }

    private fun AlgorithmProgress.shouldEmitNext(next: AlgorithmProgress): Boolean {
        if (!this.list.contentEquals(next.list)) return true
        if (this.state != next.state) return true
        // array accesses should not trigger a new emit due to performance reasons
        return this.highlights != next.highlights
    }

    @JvmName("algorithm_wait")
    private fun wait(nanos: Int) {
        val startTime = System.nanoTime()
        while (System.nanoTime() - startTime < nanos) {
            // wait exactly
            if (state != AlgorithmState.RUNNING) break
        }
    }
}