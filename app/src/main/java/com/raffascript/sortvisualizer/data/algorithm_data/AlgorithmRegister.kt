package com.raffascript.sortvisualizer.data.algorithm_data

import com.raffascript.sortvisualizer.data.algorithms.BubbleSort
import com.raffascript.sortvisualizer.data.algorithms.HeapSort
import com.raffascript.sortvisualizer.data.algorithms.InsertionSort
import com.raffascript.sortvisualizer.data.algorithms.MergeSort
import com.raffascript.sortvisualizer.data.algorithms.SelectionSort

class AlgorithmRegister {

    private val algorithms = listOf(
        AlgorithmData(
            0,
            "InsertionSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            InsertionSort::class
        ),
        AlgorithmData(
            1,
            "SelectionSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            false,
            SelectionSort::class
        ),
        AlgorithmData(
            2,
            "BubbleSort",
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            BubbleSort::class
        ),
        AlgorithmData(
            3,
            "HeapSort",
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            HeapSort::class
        ),
        AlgorithmData(
            4,
            "MergeSort",
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            true,
            MergeSort::class
        )
    )

    fun getAllAlgorithms(): List<AlgorithmData> {
        return algorithms
    }

    fun getAlgorithmById(id: Int): AlgorithmData? {
        return algorithms.find { it.id == id }
    }
}