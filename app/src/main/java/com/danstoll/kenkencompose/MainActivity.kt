package com.danstoll.kenkencompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.animation.Crossfade
import androidx.ui.core.setContent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boards = mutableListOf(listOf(listOf<KenKenCell>()))
        for (i in 3..8) {
            boards.add(KenKenGenerator().generate(i))
        }
        setContent {
            MyAppTheme {
                AppContent()
            }
        }
    }
}

@Composable
private fun AppContent() {
    Crossfade(AppStatus.currentScreen) { screen ->
        when (screen) {
            is Screen.ChooseBoard -> ChooseBoard()
            is Screen.Play -> KenKenScreen(KenKenBoard.generateBoard(screen.size))
        }
    }
}

//@Preview("MyScreen preview")
//@Composable
//fun DefaultPreview() {
//    MyAppTheme {
//        KenKenScreen()
//    }
//}
