package com.raffascript.sortvisualizer.data.viszualization

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

enum class DelayValue(val millis: Float) {
    DELAY_0(0f),
    DELAY_1(1f),
    DELAY_2(2f),
    DELAY_3(3f),
    DELAY_4(4f),
    DELAY_5(5f),
    DELAY_10(10f),
    DELAY_50(50f),
    DELAY_100(100f),
    DELAY_500(500f),
    DELAY_1000(1000f),
    DELAY_1500(1500f),
    DELAY_2000(2000f);

    fun asDuration(): Duration {
        return millis.toDouble().milliseconds
    }
    companion object {
        val default = DELAY_10
    }
}