package com.raffascript.sortvisualizer.core.util.device

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlin.math.sin
import kotlin.time.Duration

class SoundGenerator(override val soundDuration: Duration) : SoundPlayer {

    init {
        if (soundDuration.inWholeMilliseconds == 0L) {
            throw IllegalArgumentException("Sound duration must not be 0")
        }
    }

    private val duration = soundDuration.inWholeMilliseconds
    private val sampleRate = 8000
    private val numSamples = ((duration / 1000F) * sampleRate).toInt()

    private val sample = DoubleArray(numSamples)
    private val generatedSound = ByteArray(2 * numSamples)

    private var frequency: Float? = null
    private val audioTrack = buildAudioTrack()

    private var isRunning = false

    private val generator = Thread {
//        Thread.currentThread().priority = Thread.MIN_PRIORITY
        audioTrack.play()
        while (isRunning) {
            frequency?.let { freq ->
                this.frequency = null
                generateTone(freq)
                audioTrack.write(generatedSound, 0, generatedSound.size)
            }
        }
        audioTrack.stop()
        audioTrack.release()
    }

    override fun start() {
        isRunning = true
        generator.start()
    }

    override fun stop() {
        isRunning = false
    }

    override fun play(frequency: Float) {
        audioTrack.flush()
        this.frequency = frequency
    }

    private fun buildAudioTrack(): AudioTrack {
        return AudioTrack.Builder().apply {
            setTransferMode(AudioTrack.MODE_STREAM)
            setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build()
            )
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build()
            )
        }.build()
    }

    private fun generateTone(frequency: Float) {
        // fill out the array
        for (i in 0 until numSamples) {
            sample[i] = sin(2 * Math.PI * i / (sampleRate / frequency))
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        var index = 0
        for (dVal in sample) {
            // scale to maximum amplitude
            val value = (dVal * 32767).toInt().toShort()
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSound[index++] = (value.toInt() and 0x00ff).toByte()
            generatedSound[index++] = (value.toInt() and 0xff00 ushr 8).toByte()
        }
    }
}