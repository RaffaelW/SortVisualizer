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
            "GnomeSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            ::GnomeSort
        ),
        AlgorithmData(
            7,
            "CycleSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            false,
            ::CycleSort
        ),
        AlgorithmData(
            8,
            "HeapSort",
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            ::HeapSort
        ),
        AlgorithmData(
            9,
            "MergeSort",
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            true,
            ::MergeSort
        ),
        AlgorithmData(
            10,
            "RadixSort (LSD)",
            TimeComplexity.LINEAR,
            TimeComplexity.LINEAR,
            TimeComplexity.LINEAR,
            true,
            ::RadixSortLsd
        ),
        AlgorithmData(
            11,
            "QuickSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            ::QuickSort
        )
    )

    fun getAllAlgorithms(): List<AlgorithmData> {
        return algorithms
    }

    fun getAlgorithmById(id: Int): AlgorithmData? {
        return algorithms.find { it.id == id }
    }
}