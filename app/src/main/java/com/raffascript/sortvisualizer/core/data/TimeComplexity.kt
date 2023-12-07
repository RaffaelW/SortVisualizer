package com.raffascript.sortvisualizer.core.data

enum class TimeComplexity(val abbreviation: String) {
    LINEAR("O(n)"),
    QUASILINEAR("O(n log n)"),
    QUADRATIC("O(n²)");
}