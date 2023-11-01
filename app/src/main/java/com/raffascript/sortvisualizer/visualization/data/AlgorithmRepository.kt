package com.raffascript.sortvisualizer.visualization.data

import com.raffascript.sortvisualizer.core.data.algorithms.Algorithm
import com.raffascript.sortvisualizer.visualization.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

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

    fun setAlgorithm(algorithmImpl: KClass<out Algorithm>) {
        val array = generateShuffledArray(listSize)
        algorithm = algorithmImpl.primaryConstructor!!.call(array)
        state = AlgorithmState.READY
        progress = AlgorithmProgress(array, state, emptyList(), 0, 0)
    }

    fun startAlgorithm() {
        if (state != AlgorithmState.READY && state != AlgorithmState.RUNNING) {
            throw IllegalStateException("Cannot start an algorithm that is not ready")
        }
        state = AlgorithmState.RUNNING
    }

    fun pauseAlgorithm() {
        if (state != AlgorithmState.RUNNING && state != AlgorithmState.PAUSED) {
            throw IllegalStateException("Cannot pause an algorithm that is not running")
        }
        state = AlgorithmState.PAUSED
    }

    fun resumeAlgorithm() {
        if (state != AlgorithmState.PAUSED && state != AlgorithmState.RUNNING) {
            throw IllegalStateException("Cannot resume an algorithm that is not paused")
        }
        state = AlgorithmState.RUNNING
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

    suspend fun getProgressFlow(): Flow<AlgorithmProgress> = flow {
        setAlgorithm(algorithm::class)
        while (true) {
            try {
                handleStateCycle()
            } catch (e: AlgorithmCancellationException) {
                isRestartRequested = false
                setAlgorithm(algorithm::class)
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

                if (state == AlgorithmState.PAUSED) {
                    waitForState(AlgorithmState.RUNNING)
                    emit(getUpdatedProgress(state = state))
                }
                if (isRestartRequested) {
                    throw AlgorithmCancellationException()
                }

                wait(delayMillis)
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
            if (progress != startProgress) {
                emit(progress)
            }
            if (isRestartRequested) {
                throw AlgorithmCancellationException()
            }
        }
    }

    @JvmName("algorithm_wait")
    private fun wait(millis: Int) {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < millis) {
            // wait exactly
        }
    }
}