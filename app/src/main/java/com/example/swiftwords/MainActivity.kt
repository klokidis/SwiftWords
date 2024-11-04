package com.example.swiftwords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.swiftwords.ui.AppViewModelProvider
import com.example.swiftwords.ui.SwiftWordsApp
import com.example.swiftwords.ui.elements.SoundViewModel
import com.example.swiftwords.ui.theme.SwiftWordsTheme

class MainActivity : ComponentActivity() {

    private val soundViewModel: SoundViewModel by viewModels { AppViewModelProvider.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

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
    }

    override fun onResume() {
        super.onResume()
        soundViewModel.unMuteSounds()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SwiftWordsTheme {
    }
}