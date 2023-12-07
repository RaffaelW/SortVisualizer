package com.raffascript.sortvisualizer.core.data

import androidx.annotation.StringRes
import com.raffascript.sortvisualizer.R

enum class AlgorithmGroup(@StringRes val nameRes: Int) {
    INSERTION(R.string.insertion),
    SELECTION(R.string.selection),
    EXCHANGING(R.string.exchanging),
    PARTITIONING(R.string.partitioning),
    MERGING(R.string.merging);
}