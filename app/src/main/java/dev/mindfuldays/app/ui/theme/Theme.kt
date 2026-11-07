package dev.mindfuldays.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SageGreen,
    primaryContainer = SoftSage,
    secondary = MutedSand,
    background = WarmOffWhite,
    surface = PureWhite,
    onPrimary = PureWhite,
    onPrimaryContainer = PureWhite,
    onBackground = DarkForestGreen,
    onSurface = DarkForestGreen,
    onSurfaceVariant = MutedForestGreen,
    outlineVariant = MutedSand
)

private val DarkColorScheme = darkColorScheme(
    primary = LuminousSage,
    primaryContainer = DeepForestSurface,
    secondary = SoftMutedSage,
    background = DeepTranquilForest,
    surface = ElevatedForestSurface,
    onPrimary = DeepTranquilForest,
    onPrimaryContainer = WarmOffWhiteText,
    onBackground = WarmOffWhiteText,
    onSurface = WarmOffWhiteText,
    onSurfaceVariant = SoftMutedSage,
    outlineVariant = ForestOutline
)

@Composable
fun MindfulDaysTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivado: el wallpaper del dispositivo no debe alterar la paleta de la app
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
