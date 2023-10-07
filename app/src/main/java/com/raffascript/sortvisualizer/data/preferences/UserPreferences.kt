package com.raffascript.sortvisualizer.data.preferences

import com.raffascript.sortvisualizer.data.DelayValue

data class UserPreferences(
    val delay: DelayValue,
    val listSize: Int,
    val inputListSize: String
)