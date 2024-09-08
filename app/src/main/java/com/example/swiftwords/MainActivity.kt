package com.example.swiftwords

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.swiftwords.ui.SwiftWordsApp
import com.example.swiftwords.ui.elements.SoundViewModel
import com.example.swiftwords.ui.theme.SwiftWordsTheme

class MainActivity : ComponentActivity() {

    private lateinit var soundViewModel: SoundViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundViewModel = ViewModelProvider(this)[SoundViewModel::class.java]
        setContent {
            SwiftWordsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SwiftWordsApp(soundViewModel = soundViewModel)
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        soundViewModel.muteSounds()
        Log.d("klok","muted")
    }

    override fun onRestart() {
        super.onRestart()
        soundViewModel.unMuteSounds()
        Log.d("klok","unMuted")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SwiftWordsTheme {
    }
}