package com.raffascript.sortvisualizer.core.util.device

import kotlin.time.Duration

interface SoundPlayer {

    val soundDuration: Duration

    fun start()

    fun stop()

    fun play(frequency: Float)
}