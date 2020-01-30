package com.danstoll.kenkencompose

import androidx.compose.Model

sealed class Screen {
    object ChooseBoard : Screen()
    data class Play(val size: Int) : Screen()
}

@Model
object AppStatus {
    var currentScreen: Screen = Screen.ChooseBoard
}

/**
 * Temporary solution pending navigation support.
 */
fun navigateTo(destination: Screen) {
    AppStatus.currentScreen = destination
}