package com.raffascript.sortvisualizer.data.algorithm_data

enum class TimeComplexity(val abbreviation: String) {
    CONSTANT("O(1)"),
    LOGARITHMIC("O(log n)"),
    LINEAR("O(n)"),
    QUASILINEAR("O(n log n)"),
    QUADRATIC("O(n^2)"),
    INFINITE("&infin;");
}