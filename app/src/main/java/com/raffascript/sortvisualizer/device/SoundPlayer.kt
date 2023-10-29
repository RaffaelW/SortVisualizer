package com.raffascript.sortvisualizer.device

import kotlin.time.Duration

interface SoundPlayer {

    val soundDuration: Duration

    fun start()

    fun stop()

    fun play(frequency: Float)
}