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

        // Load letter sounds (A-Z) directly using R.raw
        soundIds["a"] = soundPool.load(application, R.raw.a, 1)
        soundIds["b"] = soundPool.load(application, R.raw.b, 1)
        soundIds["c"] = soundPool.load(application, R.raw.c, 1)
        soundIds["d"] = soundPool.load(application, R.raw.d, 1)
        soundIds["e"] = soundPool.load(application, R.raw.e, 1)
        soundIds["f"] = soundPool.load(application, R.raw.f, 1)
        soundIds["g"] = soundPool.load(application, R.raw.g, 1)
        soundIds["h"] = soundPool.load(application, R.raw.h, 1)
        soundIds["i"] = soundPool.load(application, R.raw.i, 1)
        soundIds["j"] = soundPool.load(application, R.raw.j, 1)
        soundIds["k"] = soundPool.load(application, R.raw.k, 1)
        soundIds["l"] = soundPool.load(application, R.raw.l, 1)
        soundIds["m"] = soundPool.load(application, R.raw.m, 1)
        soundIds["n"] = soundPool.load(application, R.raw.n, 1)
        soundIds["o"] = soundPool.load(application, R.raw.o, 1)
        soundIds["p"] = soundPool.load(application, R.raw.p, 1)
        soundIds["q"] = soundPool.load(application, R.raw.q, 1)
        soundIds["r"] = soundPool.load(application, R.raw.r, 1)
        soundIds["s"] = soundPool.load(application, R.raw.s, 1)
        soundIds["t"] = soundPool.load(application, R.raw.t, 1)
        soundIds["u"] = soundPool.load(application, R.raw.u, 1)
        soundIds["v"] = soundPool.load(application, R.raw.v, 1)
        soundIds["w"] = soundPool.load(application, R.raw.w, 1)
        soundIds["x"] = soundPool.load(application, R.raw.x, 1)
        soundIds["y"] = soundPool.load(application, R.raw.y, 1)
        soundIds["z"] = soundPool.load(application, R.raw.z, 1)
    }

    // Function to play sound by letter and pitch
    fun playLetterSound(letter: Char, pitch: Float) {
        val volume = getCurrentVolume()
        val soundId = soundIds[letter.lowercaseChar().toString()]
        if (soundId != null && volume > 0f && mainVolume != 0f) {
            soundPool.play(soundId, volume, volume, 1, 0, pitch)
        }
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
