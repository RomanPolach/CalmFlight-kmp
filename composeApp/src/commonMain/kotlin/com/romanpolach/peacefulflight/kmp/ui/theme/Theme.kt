package com.romanpolach.peacefulflight.kmp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Dark Theme color scheme
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = TealDeep,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    error = Error,
    onError = OnError,
    surfaceContainer = NavyLighter
)

// Light Theme color scheme
private val LightColorScheme = lightColorScheme(
    primary = TealDeep,
    onPrimary = PureWhite,
    primaryContainer = SkySoft,
    onPrimaryContainer = TealDeep,
    secondary = SlateMedium,
    onSecondary = PureWhite,
    secondaryContainer = SkyMist,
    onSecondaryContainer = BlueLight,
    background = SkyMist,
    onBackground = SlateDark,
    surface = SkyMist,
    onSurface = SlateDark,
    surfaceVariant = SkySoft,
    onSurfaceVariant = SlateDark,
    error = OrangeSafe,
    onError = PureWhite,
    surfaceContainer = PureWhite
)

/**
 * Peaceful Flight Theme - works on all platforms
 *
 * Note: Dynamic colors (Android 12+) are handled in androidMain if needed
 */
@Composable
fun PeacefulFlightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
