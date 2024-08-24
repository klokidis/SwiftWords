package com.example.swiftwords.ui.elements

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import com.example.swiftwords.R

class SoundViewModel(application: Application) : AndroidViewModel(application) {

    private val correctSound: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.jsfxr)
    }

    private val incorrectSound: MediaPlayer by lazy {
        MediaPlayer.create(application, R.raw.falsesound)
    }

    //variable to control volume
    private val volume: Float = 0.1f // Volume level between 0.0 and 1.0

    /*
        private val winSound: MediaPlayer by lazy {
            MediaPlayer.create(application, R.raw.win)
        }
    */
    fun playCorrectSound() {
        correctSound.seekTo(0)
        incorrectSound.seekTo(0)
        correctSound.setVolume(volume, volume)
        correctSound.start()
    }

    fun playIncorrectSound() {
        correctSound.seekTo(0)
        incorrectSound.seekTo(0)
        incorrectSound.setVolume(volume, volume)
        incorrectSound.start()
    }

    /*
        fun playWinSound() {
            winSound.start()
        }
    */
    override fun onCleared() {
        super.onCleared()
        correctSound.release()

        incorrectSound.release()
        /*
            winSound.release()
            */
    }
}
