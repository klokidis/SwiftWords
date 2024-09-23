package com.example.swiftwords

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.swiftwords.ui.SwiftWordsApp
import com.example.swiftwords.ui.elements.SoundViewModel
import com.example.swiftwords.ui.theme.SwiftWordsTheme

class MainActivity : ComponentActivity() {

    private lateinit var soundViewModel: SoundViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        soundViewModel = ViewModelProvider(this)[SoundViewModel::class.java]
        setContent {
            SwiftWordsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SwiftWordsApp()
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        soundViewModel.muteSounds()
        Log.d("klok2","muted")
    }

    override fun onResume() {
        super.onResume()
        soundViewModel.unMuteSounds()
        Log.d("klok2","unMuted")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SwiftWordsTheme {
    }
}