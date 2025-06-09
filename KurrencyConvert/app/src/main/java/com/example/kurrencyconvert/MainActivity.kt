package com.example.kurrencyconvert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.kurrencyconvert.ui.screen.CurrencyScreen
import com.example.kurrencyconvert.ui.screen.HistoryScreen
import com.example.kurrencyconvert.ui.theme.KurrencyConvertTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KurrencyConvertTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("converter") }

                    when (currentScreen) {
                        "converter" -> CurrencyScreen(
                            onNavigateToHistory = { currentScreen = "history" }
                        )
                        "history" -> HistoryScreen(
                            onNavigateBack = { currentScreen = "converter" }
                        )
                    }
                }
            }
        }
    }
}