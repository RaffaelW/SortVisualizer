package com.raffascript.sortvisualizer.core.data

import com.raffascript.sortvisualizer.core.data.algorithms.*

class AlgorithmRegister {

    private val algorithms = listOf(
        AlgorithmData(
            0,
            "InsertionSort",
            AlgorithmGroup.INSERTION,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            ::InsertionSort
        ),
        AlgorithmData(
            1,
            "ShellSort",
            AlgorithmGroup.INSERTION,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            ::ShellSort
        ),
        AlgorithmData(
            2,
            "SelectionSort",
            AlgorithmGroup.SELECTION,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            false,
            ::SelectionSort
        ),
        AlgorithmData(
            3,
            "BubbleSort",
            AlgorithmGroup.EXCHANGING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            ::BubbleSort
        ),
        AlgorithmData(
            4,
            "ShakerSort",
            AlgorithmGroup.EXCHANGING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            ::ShakerSort
        ),
        AlgorithmData(
            5,
            "CombSort",
            AlgorithmGroup.EXCHANGING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUASILINEAR,
            false,
            ::CombSort
        ),
        AlgorithmData(
            6,
            "GnomeSort",
            AlgorithmGroup.EXCHANGING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            ::GnomeSort
        ),
        AlgorithmData(
            7,
            "CycleSort",
            AlgorithmGroup.SELECTION,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            false,
            ::CycleSort
        ),
        AlgorithmData(
            8,
            "HeapSort",
            AlgorithmGroup.SELECTION,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            ::HeapSort
        ),
        AlgorithmData(
            9,
            "MergeSort",
            AlgorithmGroup.MERGING,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            true,
            ::MergeSort
        ),
        AlgorithmData(
            10,
            "RadixSort (LSD)",
            AlgorithmGroup.SELECTION,
            TimeComplexity.LINEAR,
            TimeComplexity.LINEAR,
            TimeComplexity.LINEAR,
            true,
            ::RadixSortLsd
        ),
        AlgorithmData(
            11,
            "QuickSort",
            AlgorithmGroup.PARTITIONING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            ::QuickSort
        )
    )

    fun getAllAlgorithms(): Map<AlgorithmGroup, List<AlgorithmData>> {
        return algorithms.groupBy { it.group }
    }

    fun getAlgorithmById(id: Int): AlgorithmData? {
        return algorithms.find { it.id == id }
    }
}