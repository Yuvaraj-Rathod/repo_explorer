package com.example.repoexplorer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val EvHubDarkColorScheme = darkColorScheme(
    primary = FuturisticGreen,
    onPrimary = White,
    secondary = CharcoalBlue,
    onSecondary = LightGray,
    background = MidnightBlue,
    onBackground = White,
    surface = SlateBlue,
    onSurface = LightGray
)

@Composable
fun RepoExplorer(content: @Composable () -> Unit) {
    val view = LocalView.current
    val systemUiController = rememberSystemUiController()

    if (!view.isInEditMode) {
        SideEffect {
            systemUiController.setStatusBarColor(color = SlateBlue)
            systemUiController.setNavigationBarColor(color = MidnightBlue)
//            systemUiController.isStatusBarVisible = false;
        }
    }
    MaterialTheme(
        colorScheme = EvHubDarkColorScheme,
        typography = AppTypography,
        content = content
    )
}