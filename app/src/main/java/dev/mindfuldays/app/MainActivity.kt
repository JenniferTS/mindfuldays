package dev.mindfuldays.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.mindfuldays.app.ui.home.HomeScreen
import dev.mindfuldays.app.ui.settings.SettingsScreen
import dev.mindfuldays.app.ui.theme.MindfulDaysTheme
import dev.mindfuldays.app.ui.timer.TimerScreen
import dev.mindfuldays.app.ui.viewmodel.MindfulnessViewModel

enum class Screen { Home, Timer, Settings }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindfulDaysTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val viewModel: MindfulnessViewModel = viewModel()
                    var screen by rememberSaveable { mutableStateOf(Screen.Home) }

                    BackHandler(enabled = screen != Screen.Home) { screen = Screen.Home }

                    when (screen) {
                        Screen.Home -> HomeScreen(
                            viewModel = viewModel,
                            onNavigateToSettings = { screen = Screen.Settings },
                            onNavigateToTimer = { screen = Screen.Timer }
                        )
                        Screen.Timer -> TimerScreen(onNavigateBack = { screen = Screen.Home })
                        Screen.Settings -> SettingsScreen(onNavigateBack = { screen = Screen.Home })
                    }
                }
            }
        }
    }
}
