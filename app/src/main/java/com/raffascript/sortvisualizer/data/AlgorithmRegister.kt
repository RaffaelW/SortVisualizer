package com.raffascript.sortvisualizer.data

import com.raffascript.sortvisualizer.data.algorithms.BubbleSort
import com.raffascript.sortvisualizer.data.algorithms.InsertionSort
import com.raffascript.sortvisualizer.data.algorithms.SelectionSort

class AlgorithmRegister {

    private val algorithms = listOf(
        AlgorithmData(0, "InsertionSort", InsertionSort::class),
        AlgorithmData(1, "SelectionSort", SelectionSort::class),
        AlgorithmData(2, "BubbleSort", BubbleSort::class)
    )

    fun getAllAlgorithms(): List<AlgorithmData> {
        return algorithms
    }

    fun getAlgorithmById(id: Int): AlgorithmData? {
        return algorithms.find { it.id == id }
    }
}