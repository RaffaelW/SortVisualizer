package com.raffascript.sortvisualizer.data.algorithm_data

import android.text.Html

enum class TimeComplexity(val abbreviation: String) {
    CONSTANT("O(1)"),
    LOGARITHMIC("O(log n)"),
    LINEAR("O(n)"),
    QUASILINEAR("O(n log n)"),
    QUADRATIC("O(n²)"),
    INFINITE("∞");

    init {
        Html.fromHtml("fdf", 0)
    }
}