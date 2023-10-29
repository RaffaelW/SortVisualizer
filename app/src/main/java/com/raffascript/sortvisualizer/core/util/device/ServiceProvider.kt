package com.raffascript.sortvisualizer.core.util.device

import kotlin.time.Duration

object ServiceProvider {

    fun getSoundPlayer(soundDuration: Duration): SoundPlayer {
        return SoundGenerator(soundDuration)
    }

}