package io.github.ahmadardani.kinoki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.ahmadardani.kinoki.ui.screens.AddCardScreen
import io.github.ahmadardani.kinoki.ui.screens.CreateDeckScreen
import io.github.ahmadardani.kinoki.ui.screens.HomeScreen
import io.github.ahmadardani.kinoki.ui.screens.SettingsScreen
import io.github.ahmadardani.kinoki.ui.theme.KinokiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KinokiTheme {
                var currentScreen by remember { mutableStateOf("home") }

                when (currentScreen) {
                    "home" -> HomeScreen(
                        onNavigateToSettings = { currentScreen = "settings" },
                        onAddDeckClick = { currentScreen = "create_deck" }
                    )
                    "settings" -> SettingsScreen(
                        onNavigateToHome = { currentScreen = "home" }
                    )
                    "create_deck" -> CreateDeckScreen(
                        onNavigateBack = { currentScreen = "home" },
                        onCreateDeck = { deckName ->
                            currentScreen = "add_card"
                        }
                    )
                    "add_card" -> AddCardScreen(
                        onNavigateBack = {
                            currentScreen = "home"
                        },
                        onAddCard = { front, back, center ->
                            println("Card Added: $front - $center - $back")

                        }
                    )
                }
            }
        }
    }
}