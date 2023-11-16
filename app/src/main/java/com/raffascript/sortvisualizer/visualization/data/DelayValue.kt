package com.raffascript.sortvisualizer.visualization.data

enum class DelayValue(val millis: Int) {
    DELAY_0(0),
    DELAY_1(1),
    DELAY_2(2),
    DELAY_4(4),
    DELAY_5(5),
    DELAY_10(10),
    DELAY_50(50),
    DELAY_100(100),
    DELAY_500(500),
    DELAY_1000(1000),
    DELAY_1500(1500),
    DELAY_2000(2000);

    companion object {
        val default = DELAY_10
    }
}