package com.example.swiftwords.ui.elements

import android.app.Application
import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.lifecycle.AndroidViewModel
import com.example.swiftwords.R

class SoundViewModel(application: Application) : AndroidViewModel(application) {

    private val correctSound: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.jsfxr)
    }

    private val incorrectSound: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.falsesound)
    }
    private val changeSound: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.change)
    }

    private val bipSound: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.bip)
    }
    private val v1: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.v1)
    }
    private val v2: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.v2)
    }
    private val v3: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.v3)
    }
    private val v4: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.v4)
    }
    private val v5: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.v5)
    }

    // Variable to control volume
    private val volume: Float = 0.1f // Volume level between 0.0 and 1.0
    // Variable to control volume
    private val volumeVowels: Float = 1.7f // Volume level between 0.0 and 1.0

    fun playCorrectSound() {
        correctSound.seekTo(0)
        correctSound.setVolume(volume, volume)
        correctSound.start()
    }

    fun playIncorrectSound() {
        incorrectSound.seekTo(0)
        incorrectSound.setVolume(volume, volume)
        incorrectSound.start()
    }

    fun playChangeSound() {
        changeSound.seekTo(0)
        changeSound.setVolume(volume, volume)
        changeSound.start()
    }

    fun playBipSound(pitch: Float) {
        bipSound.setVolume(volume, volume)
        setPitch(bipSound, pitch)
        bipSound.start()
    }
    fun playV1(pitch: Float) {
        v1.setVolume(volumeVowels, volumeVowels)
        setPitch(v1, pitch)
        v1.start()
    }

    fun playV2(pitch: Float) {
        v2.setVolume(volumeVowels, volumeVowels)
        setPitch(v2, pitch)
        v2.start()
    }

    fun playV3(pitch: Float) {
        v3.setVolume(volumeVowels, volumeVowels)
        setPitch(v3, pitch)
        v3.start()
    }
    fun playV4(pitch: Float) {
        v4.setVolume(volumeVowels, volumeVowels)
        setPitch(v4, pitch)
        v4.start()
    }
    fun playV5(pitch: Float) {
        v5.setVolume(volumeVowels, volumeVowels)
        setPitch(v5, pitch)
        v5.start()
    }

    private fun setPitch(mediaPlayer: MediaPlayer, pitch: Float) {
        val params = PlaybackParams()
        params.pitch = pitch
        mediaPlayer.playbackParams = params
    }

    override fun onCleared() {
        super.onCleared()
        correctSound.release()
        incorrectSound.release()
        bipSound.release()
    }
}
