package dev.mindfuldays.app.screenshots

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import dev.mindfuldays.app.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Capturas de las 3 pantallas generadas con Robolectric + Roborazzi (sin emulador).
 * Ejecutar: ./gradlew testDebugUnitTest --tests "*ScreenshotsTest*"
 * Las imágenes quedan en screenshots/generated/.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
abstract class ScreenshotsBase(private val prefix: String) {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()

    private fun shot(name: String) {
        rule.waitForIdle()
        rule.onRoot().captureRoboImage("../screenshots/generated/${prefix}_$name.png")
    }

    private fun click(desc: String) {
        rule.onNodeWithContentDescription(desc, useUnmergedTree = true).performClick()
    }

    @Test fun home() = shot("home")

    @Test fun timer() {
        rule.onNode(androidx.compose.ui.test.hasText("⏱️", substring = true)).performClick()
        shot("timer")
    }

    @Test fun settings() {
        click(rule.activity.getString(dev.mindfuldays.app.R.string.cd_settings))
        shot("settings")
    }
}

@Config(sdk = [35], qualifiers = "es-w411dp-h891dp-xxhdpi")
class ScreenshotsEsLight : ScreenshotsBase("es_light")

@Config(sdk = [35], qualifiers = "es-w411dp-h891dp-night-xxhdpi")
class ScreenshotsEsDark : ScreenshotsBase("es_dark")
