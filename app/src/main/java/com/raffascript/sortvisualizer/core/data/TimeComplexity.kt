package com.raffascript.sortvisualizer.core.data

import android.text.Html

enum class TimeComplexity(val abbreviation: String) {
    LINEAR("O(n)"),
    QUASILINEAR("O(n log n)"),
    QUADRATIC("O(n²)");

    init {
        Html.fromHtml("fdf", 0)
    }
}