package com.example.swiftwords.ui.elements

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftwords.R
import kotlinx.coroutines.launch

class SoundViewModel(application: Application) : AndroidViewModel(application) {

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .build()
    private val soundIds = mutableMapOf<String, Int>()

    private var mainVolume = 1f

    init {
        viewModelScope.launch {
            // Load sounds into the SoundPool
            soundIds["correct"] = soundPool.load(application, R.raw.jsfxr, 1)
            soundIds["incorrect"] = soundPool.load(application, R.raw.falsesound, 1)
            soundIds["change"] = soundPool.load(application, R.raw.change, 1)
        }
    }

    fun loadLettersSound() {
        if (soundIds.size < 4) { //load them only one time
            val app = getApplication<Application>() // Get the Application object directly
            viewModelScope.launch {
                // Map of letter to corresponding sound resource ID
                val letterToResId = mapOf(
                    "a" to R.raw.a,
                    "b" to R.raw.b,
                    "c" to R.raw.c,
                    "d" to R.raw.d,
                    "e" to R.raw.e,
                    "f" to R.raw.f,
                    "g" to R.raw.g,
                    "h" to R.raw.h,
                    "i" to R.raw.i,
                    "j" to R.raw.j,
                    "k" to R.raw.k,
                    "l" to R.raw.l,
                    "m" to R.raw.m,
                    "n" to R.raw.n,
                    "o" to R.raw.o,
                    "p" to R.raw.p,
                    "q" to R.raw.q,
                    "r" to R.raw.r,
                    "s" to R.raw.s,
                    "t" to R.raw.t,
                    "u" to R.raw.u,
                    "v" to R.raw.v,
                    "w" to R.raw.w,
                    "x" to R.raw.x,
                    "y" to R.raw.y,
                    "z" to R.raw.z
                )

                // Load each sound only if it is not already loaded
                for ((letter, resId) in letterToResId) {
                    // Check if the sound for the letter is already loaded
                    if (!soundIds.containsKey(letter)) {
                        soundIds[letter] = soundPool.load(app, resId, 1)
                    }
                }
            }
        }
    }

    fun releaseAllAlphabetSounds() {
        viewModelScope.launch {
            // Loop through the alphabet
            for (letter in 'a'..'z') {
                releaseSound(letter)  // Call the existing releaseSound method for each letter
            }
        }
    }

    private fun releaseSound(letter: Char) {
        val soundId = soundIds[letter.lowercaseChar().toString()]
        if (soundId != null) {
            soundPool.unload(soundId)
            soundIds.remove(letter.lowercaseChar().toString())
        }
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