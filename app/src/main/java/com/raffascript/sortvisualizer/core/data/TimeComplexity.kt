package com.raffascript.sortvisualizer.core.data

import androidx.annotation.StringRes
import com.raffascript.sortvisualizer.R

enum class TimeComplexity(val abbreviation: String, @StringRes val fullName: Int) {
    LINEAR("O(n)", R.string.linear),
    QUASILINEAR("O(n log n)", R.string.quasilinear),
    QUADRATIC("O(n²)", R.string.quadratic);
}