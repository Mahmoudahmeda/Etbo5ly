package com.example.etbo5ly.ui.theme

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
    primary = Turquoise,           // The main accent color in dark mode, used for primary buttons and active states.
    secondary = TurquoiseShade,         // Secondary color, here we use the same turquoise for consistency.
    tertiary = MidnightBlueSurface, // Used for decorative elements or less prominent surfaces.
    background = MidnightBlue,     // The deep blue background for all screens in dark mode.
    surface = MidnightBlueSurface,  // A slightly lighter blue for cards, sheets, and dialogs.
    surfaceVariant = LighterBlueSurface, // Slightly Lighter cream for TextFields/Unselected buttons
    onPrimary = MidnightBlue,      // Text color on top of primary (Turquoise) - dark blue for better contrast.
    onSecondary = MidnightBlue,    // Text color on top of secondary.
    onTertiary = Color.White,      // Text color on top of tertiary elements.
    onBackground = Color.White,    // Main text color for content on the midnight blue background.
    onSurface = Color.White,        // Main text color for content inside cards or surfaces.
    onSurfaceVariant = Color(0xFF8E8E93) // Muted hint text color (SubtitleGray)

)

private val LightColorScheme = lightColorScheme(
    primary = DeepRed,             // Main brand color: Deep Red
    secondary = DeepRedShade,
    tertiary = WarmCreamSurface,
    background = WarmCream,        // Screen background: Warm Cream
    surface = WarmCreamSurface,    // Card background: White
    surfaceVariant = Color(0xFFF5E6D3), // Slightly darker cream for TextFields/Unselected buttons
    onPrimary = Color.White,       // Text on Deep Red
    onSecondary = Color.White,
    onTertiary = Color.Black,      // Dark text for content on tertiary surfaces.
    onBackground = Color.Black,    // Main text color
    onSurface = Color.Black,
    onSurfaceVariant = Color(0xFF5D4037) // Deep brown hint text (matches warm theme)
)

@Composable
fun Etbo5lyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set to false to force your custom Turquoise/Red colors
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
