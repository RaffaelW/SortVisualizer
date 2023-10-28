package com.raffascript.sortvisualizer.device

import kotlin.time.Duration

object ServiceProvider {

    fun getSoundPlayer(soundDuration: Duration): SoundPlayer {
        return SoundGenerator(soundDuration)
    }

}