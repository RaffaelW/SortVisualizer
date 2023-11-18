package com.raffascript.sortvisualizer.core.data

import com.raffascript.sortvisualizer.core.data.algorithms.*

class AlgorithmRegister {

    private val algorithms = listOf(
        AlgorithmData(
            0,
            "InsertionSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            ::InsertionSort
        ),
        AlgorithmData(
            1,
            "ShellSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            ::ShellSort
        ),
        AlgorithmData(
            2,
            "SelectionSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            false,
            ::SelectionSort
        ),
        AlgorithmData(
            3,
            "BubbleSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            ::BubbleSort
        ),
        AlgorithmData(
            4,
            "ShakerSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            ::ShakerSort
        ),
        AlgorithmData(
            5,
            "CombSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUASILINEAR,
            false,
            ::CombSort
        ),
        AlgorithmData(
            6,
            "HeapSort",
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            ::HeapSort
        ),
        AlgorithmData(
            7,
            "MergeSort",
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            true,
            ::MergeSort
        )
    )
    fun getAllAlgorithms(): List<AlgorithmData> {
        return algorithms
    }

    fun getAlgorithmById(id: Int): AlgorithmData? {
        return algorithms.find { it.id == id }
    }
}