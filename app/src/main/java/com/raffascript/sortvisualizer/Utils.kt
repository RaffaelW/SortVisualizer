package com.raffascript.sortvisualizer

fun IntRange.convert(number: Int, target: IntRange): Float {
    val ratio = number.toFloat() / (endInclusive - start)
    return (ratio * (target.last - target.first))
}