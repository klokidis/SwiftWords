package com.example.swiftwords.ui.elements

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.lifecycle.AndroidViewModel
import com.example.swiftwords.R

class SoundViewModel(application: Application) : AndroidViewModel(application) {


    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // Use the system media volume
    private fun getSystemVolume(): Float {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        return currentVolume / maxVolume.toFloat()
    }
    var mainVolume = 1f

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

    fun playCorrectSound() {
        val systemVolume = getSystemVolume()

        // Check if volume is 0 before playing sound
        if (systemVolume > 0f && mainVolume!=0f) {
            correctSound.seekTo(0)
            correctSound.setVolume(systemVolume, systemVolume)
            correctSound.start()
        } else {
            correctSound.pause()  // Stop playback immediately if volume is 0
        }
    }

    fun playIncorrectSound() {
        val systemVolume = getSystemVolume()
        if (systemVolume > 0f && mainVolume != 0f) {
            incorrectSound.seekTo(0)
            incorrectSound.setVolume(systemVolume, systemVolume)
            incorrectSound.start()
        } else {
            incorrectSound.pause()
        }
    }

    fun playChangeSound() {
        val systemVolume = getSystemVolume()
        if (systemVolume > 0f && mainVolume != 0f) {
            changeSound.seekTo(0)
            changeSound.setVolume(systemVolume, systemVolume)
            changeSound.start()
        } else {
            changeSound.pause()
        }
    }

    fun playBipSound(pitch: Float) {
        val systemVolume = getSystemVolume()
        if (systemVolume > 0f && mainVolume != 0f) {
            bipSound.seekTo(0)
            bipSound.setVolume(systemVolume, systemVolume)
            setPitch(bipSound, pitch)
            bipSound.start()
        } else {
            bipSound.pause()
        }
    }

    fun playV1(pitch: Float) {
        val systemVolume = getSystemVolume()
        if (systemVolume > 0f && mainVolume != 0f) {
            v1.seekTo(0)
            v1.setVolume(systemVolume, systemVolume)
            setPitch(v1, pitch)
            v1.start()
        } else {
            v1.pause()
        }
    }

    fun playV2(pitch: Float) {
        val systemVolume = getSystemVolume()
        if (systemVolume > 0f && mainVolume != 0f) {
            v2.seekTo(0)
            v2.setVolume(systemVolume, systemVolume)
            setPitch(v2, pitch)
            v2.start()
        } else {
            v2.pause()
        }
    }

    fun playV3(pitch: Float) {
        val systemVolume = getSystemVolume()
        if (systemVolume > 0f && mainVolume != 0f) {
            v3.seekTo(0)
            v3.setVolume(systemVolume, systemVolume)
            setPitch(v3, pitch)
            v3.start()
        } else {
            v3.pause()
        }
    }

    fun playV4(pitch: Float) {
        val systemVolume = getSystemVolume()
        if (systemVolume > 0f && mainVolume != 0f) {
            v4.seekTo(0)
            v4.setVolume(systemVolume, systemVolume)
            setPitch(v4, pitch)
            v4.start()
        } else {
            v4.pause()
        }
    }

    fun playV5(pitch: Float) {
        val systemVolume = getSystemVolume()
        if (systemVolume > 0f && mainVolume != 0f) {
            v5.seekTo(0)
            v5.setVolume(systemVolume, systemVolume)
            setPitch(v5, pitch)
            v5.start()
        } else {
            v5.pause()
        }
    }

    private fun setPitch(mediaPlayer: MediaPlayer, pitch: Float) {
        val params = PlaybackParams()
        params.pitch = pitch
        mediaPlayer.playbackParams = params
    }

    private fun setVolume() {
        val volume = getSystemVolume()
        incorrectSound.setVolume(volume, volume)
        correctSound.setVolume(volume, volume)
        changeSound.setVolume(volume, volume)
        bipSound.setVolume(volume, volume)
        v1.setVolume(volume, volume)
        v2.setVolume(volume, volume)
        v3.setVolume(volume, volume)
        v4.setVolume(volume, volume)
        v5.setVolume(volume, volume)
    }

    fun muteSounds() {
        mainVolume = 0f
        setVolume()
    }

    fun unMuteSounds() {
        mainVolume = 1f
        setVolume()
    }

    override fun onCleared() {
        super.onCleared()
        correctSound.release()
        incorrectSound.release()
        bipSound.release()
        v1.release()
        v2.release()
        v3.release()
        v4.release()
        v5.release()
    }
}
