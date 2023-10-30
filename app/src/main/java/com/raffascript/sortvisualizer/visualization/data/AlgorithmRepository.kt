package com.raffascript.sortvisualizer.visualization.data

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
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AlgorithmRepository(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    private lateinit var algorithm: Algorithm
    private var state = AlgorithmState.UNINITIALIZED
    private var delayMillis = 0
    private var listSize = 0

    private lateinit var progress: AlgorithmProgress

    init {
        CoroutineScope(Dispatchers.Default).launch {
            userPreferencesRepository.getUserPreferencesFlow().collect { preferences ->
                delayMillis = preferences.delay.millis
                if (listSize != preferences.listSize) {
                    listSize = preferences.listSize
                    setAlgorithm(algorithm::class)
                }
            }
        }
    }

    fun setAlgorithm(algorithmImpl: KClass<out Algorithm>) {
        val array = IntArray(listSize).apply {
            forEachIndexed { index, _ ->
                this[index] = index + 1
            }
            shuffle()
        }
        algorithm = algorithmImpl.primaryConstructor!!.call(array)
        state = AlgorithmState.READY
        progress = AlgorithmProgress(array, state, emptyList(), 0, 0)
    }

    fun startAlgorithm() {
        state = AlgorithmState.RUNNING
    }

    fun pauseAlgorithm() {
        state = AlgorithmState.PAUSED
    }

    fun resumeAlgorithm() {
        state = AlgorithmState.RUNNING
    }

    fun restartAlgorithm() {
        setAlgorithm(algorithm::class)
    }

    fun changeDelay(delayMillis: Int) {
        this.delayMillis = delayMillis
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
        emit(getUpdatedProgress(algorithm.getListValue(), state, emptyList()))

        waitForState(AlgorithmState.RUNNING)
        emit(getUpdatedProgress(state = state))

        algorithm.startSorting(
            onStep = { list, highlights, arrayAccesses, comparisons ->
                currentCoroutineContext().ensureActive()

                emit(getUpdatedProgress(list, state, highlights, arrayAccesses, comparisons))

                if (state == AlgorithmState.PAUSED) {
                    waitForState(AlgorithmState.RUNNING)
                    emit(getUpdatedProgress(state = state))
                }

                wait(delayMillis)
            },
            onFinish = { list, arrayAccesses, comparisons ->
                state = AlgorithmState.FINISHED
                emit(
                    getUpdatedProgress(
                        list = list,
                        state = state,
                        arrayAccesses = arrayAccesses,
                        comparisons = comparisons
                    )
                )
            }
        )
    }

    private suspend fun FlowCollector<AlgorithmProgress>.waitForState(algorithmState: AlgorithmState) {
        val startProgress = progress.copy()
        while (state != algorithmState) {
            yield() // wait
            if (progress != startProgress) {
                emit(progress)
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