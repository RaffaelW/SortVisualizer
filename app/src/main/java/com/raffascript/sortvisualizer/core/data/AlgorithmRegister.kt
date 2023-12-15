package com.raffascript.sortvisualizer.core.data

import com.raffascript.sortvisualizer.core.data.algorithms.*

class AlgorithmRegister {

    private val algorithms = listOf(
        AlgorithmData(
            0,
            "Insertion Sort",
            AlgorithmGroup.INSERTION,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            "https://en.wikipedia.org/wiki/Insertion_sort",
            ::InsertionSort
        ),
        AlgorithmData(
            1,
            "Shellsort",
            AlgorithmGroup.INSERTION,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            "https://en.wikipedia.org/wiki/Shellsort",
            ::ShellSort
        ),
        AlgorithmData(
            2,
            "Selection Sort",
            AlgorithmGroup.SELECTION,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            false,
            "https://en.wikipedia.org/wiki/Selection_sort",
            ::SelectionSort
        ),
        AlgorithmData(
            3,
            "Bubble Sort",
            AlgorithmGroup.EXCHANGING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            "https://en.wikipedia.org/wiki/Bubble_sort",
            ::BubbleSort
        ),
        AlgorithmData(
            4,
            "Cocktail Shaker Sort",
            AlgorithmGroup.EXCHANGING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            "https://en.wikipedia.org/wiki/Cocktail_shaker_sort",
            ::ShakerSort
        ),
        AlgorithmData(
            5,
            "Comb Sort",
            AlgorithmGroup.EXCHANGING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUASILINEAR,
            false,
            "https://en.wikipedia.org/wiki/Comb_sort",
            ::CombSort
        ),
        AlgorithmData(
            6,
            "Gnome Sort",
            AlgorithmGroup.EXCHANGING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.LINEAR,
            true,
            "https://en.wikipedia.org/wiki/Gnome_sort",
            ::GnomeSort
        ),
        AlgorithmData(
            7,
            "Cycle Sort",
            AlgorithmGroup.SELECTION,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            false,
            "https://en.wikipedia.org/wiki/Cycle_sort",
            ::CycleSort
        ),
        AlgorithmData(
            8,
            "Heapsort",
            AlgorithmGroup.SELECTION,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            "https://en.wikipedia.org/wiki/Heapsort",
            ::HeapSort
        ),
        AlgorithmData(
            9,
            "Merge Sort",
            AlgorithmGroup.MERGING,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            true,
            "https://en.wikipedia.org/wiki/Merge_sort",
            ::MergeSort
        ),
        AlgorithmData(
            10,
            "Radix Sort (LSD)",
            AlgorithmGroup.SELECTION,
            TimeComplexity.LINEAR,
            TimeComplexity.LINEAR,
            TimeComplexity.LINEAR,
            true,
            "https://en.wikipedia.org/wiki/Radix_sort",
            ::RadixSortLsd
        ),
        AlgorithmData(
            11,
            "Quicksort",
            AlgorithmGroup.PARTITIONING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUASILINEAR,
            TimeComplexity.QUASILINEAR,
            false,
            "https://en.wikipedia.org/wiki/Quicksort",
            ::QuickSort
        ),
        AlgorithmData(
            12,
            "Pancake Sort",
            AlgorithmGroup.EXCHANGING,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            TimeComplexity.QUADRATIC,
            false,
            "https://en.wikipedia.org/wiki/Pancake_sorting",
            ::PancakeSort
        )
    )

    fun getAllAlgorithms(): Map<AlgorithmGroup, List<AlgorithmData>> {
        return algorithms.groupBy { it.group }
    }

    fun getAlgorithmById(id: Int): AlgorithmData? {
        return algorithms.find { it.id == id }
    }
}