package io.github.ahmadardani.kinoki.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = KinokiDarkBlue,
    secondary = KinokiInactiveIcon,
    tertiary = KinokiDarkBlue,
    background = KinokiBackground,
    surface = KinokiWhite
)

private val LightColorScheme = lightColorScheme(
    primary = KinokiDarkBlue,
    secondary = KinokiInactiveIcon,
    tertiary = KinokiDarkBlue,
    background = KinokiBackground,
    surface = KinokiWhite,
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = KinokiDarkBlue
)

@Composable
fun KinokiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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