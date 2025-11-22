package com.example.raksha_astra.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
private val DarkColorScheme = darkColorScheme(
    primary = PinkPrimary,
    onPrimary = OnDark,
    secondary = MaroonSecondary,
    onSecondary = OnDark,
    background = SurfaceDark,
    onBackground = OnDark,
    surface = CardDark,
    onSurface = OnDark,
    error = Danger
)
private val LightColors = lightColorScheme(
    primary = PinkMain,
    secondary = BlueMain,
    background = BlueLight2,
    surface = BlueLight1,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = BlueDark1,
    onSurface = BlueDark1
)
@Composable
//fun Raksha_AstraTheme(content: @Composable () -> Unit) {
//    MaterialTheme(
//        colorScheme = LightColors,
//        typography = Typography(),
//        content = content
//    )
fun Raksha_AstraTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
