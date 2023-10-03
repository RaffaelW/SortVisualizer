package com.raffascript.sortvisualizer.presentation.visualizer.chart

/**
 * Compares two lists and returns a Hashmap containing the differences.
 * The indices of the HashMap are the indices of the lists that contain different values.
 * The value of the HashMap is the value from the second list.
 * @param list1 the first list that should be compared
 * @param list2 the second list that should be compared, the values from the returned HashMap are taken from this list
 * @return The differences of the lists or null if the sizes of the lists are different
 */
fun calculateChanges(list1: IntArray, list2: IntArray): HashMap<Int, Int>? {
    if (list1.size != list2.size) return null

    val diff = hashMapOf<Int, Int>()
    list1.forEachIndexed { i, oldValue ->
        val newValue = list2[i]
        if (oldValue != newValue) {
            diff[i] = newValue
        }
    }

    return diff
}