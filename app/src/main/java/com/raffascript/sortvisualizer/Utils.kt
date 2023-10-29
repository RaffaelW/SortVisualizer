package com.raffascript.sortvisualizer

fun shuffledListOfSize(size: Int): IntArray {
    val array = IntArray(size)
    for (i in 0 until size) {
        array[i] = i + 1
    }
    array.shuffle()
    return array
}

fun IntRange.convert(number: Int, target: IntRange): Float {
    val ratio = number.toFloat() / (endInclusive - start)
    return (ratio * (target.last - target.first))
}