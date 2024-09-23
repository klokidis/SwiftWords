package com.example.swiftwords.ui.elements

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import androidx.lifecycle.AndroidViewModel
import com.example.swiftwords.R

class SoundViewModel(application: Application) : AndroidViewModel(application) {

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .build()
    private val soundIds = mutableMapOf<String, Int>()

    private var mainVolume = 1f

    init {

        // Load sounds into the SoundPool
        soundIds["correct"] = soundPool.load(application, R.raw.jsfxr, 1)
        soundIds["incorrect"] = soundPool.load(application, R.raw.falsesound, 1)
        soundIds["change"] = soundPool.load(application, R.raw.change, 1)
        soundIds["bip"] = soundPool.load(application, R.raw.bip, 1)
        soundIds["v1"] = soundPool.load(application, R.raw.v1, 1)
        soundIds["v2"] = soundPool.load(application, R.raw.v2, 1)
        soundIds["v3"] = soundPool.load(application, R.raw.v3, 1)
        soundIds["v4"] = soundPool.load(application, R.raw.v4, 1)
        soundIds["v5"] = soundPool.load(application, R.raw.v5, 1)
    }

    // Use the system media volume
    private fun getSystemVolume(): Float {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        return currentVolume / maxVolume.toFloat()
    }

    fun playCorrectSound() {
        val volume = getCurrentVolume()
        if (volume > 0f && mainVolume != 0f) {
            soundPool.play(soundIds["correct"]!!, volume, volume, 1, 0, 1f)
        }
    }

    fun playIncorrectSound() {
        val volume = getCurrentVolume()
        if (volume > 0f && mainVolume != 0f) {
            soundPool.play(soundIds["incorrect"]!!, volume, volume, 1, 0, 1f)
        }
    }

    fun playChangeSound() {
        val volume = getCurrentVolume()
        if (volume > 0f && mainVolume != 0f) {
            soundPool.play(soundIds["change"]!!, volume, volume, 1, 0, 1f)
        }
    }

    fun playBipSound(pitch: Float) {
        val volume = getCurrentVolume()
        if (volume > 0f && mainVolume != 0f) {
            soundPool.play(soundIds["bip"]!!, volume, volume, 1, 0, pitch)
        }
    }

    fun playV1(pitch: Float) {
        val volume = getCurrentVolume()
        if (volume > 0f && mainVolume != 0f) {
            soundPool.play(soundIds["v1"]!!, volume, volume, 1, 0, pitch)
        }
    }

    fun playV2(pitch: Float) {
        val volume = getCurrentVolume()
        if (volume > 0f && mainVolume != 0f) {
            soundPool.play(soundIds["v2"]!!, volume, volume, 1, 0, pitch)
        }
    }

    fun playV3(pitch: Float) {
        val volume = getCurrentVolume()
        if (volume > 0f && mainVolume != 0f) {
            soundPool.play(soundIds["v3"]!!, volume, volume, 1, 0, pitch)
        }
    }

    fun playV4(pitch: Float) {
        val volume = getCurrentVolume()
        if (volume > 0f && mainVolume != 0f) {
            soundPool.play(soundIds["v4"]!!, volume, volume, 1, 0, pitch)
        }
    }

    fun playV5(pitch: Float) {
        val volume = getCurrentVolume()
        if (volume > 0f && mainVolume != 0f) {
            soundPool.play(soundIds["v5"]!!, volume, volume, 1, 0, pitch)
        }
    }

    private fun getCurrentVolume(): Float {
        return getSystemVolume() * mainVolume
    }

    fun muteSounds() {
        mainVolume = 0f
    }

    fun unMuteSounds() {
        mainVolume = 1f
    }

    override fun onCleared() {
        super.onCleared()
        soundPool.release()
    }
}
