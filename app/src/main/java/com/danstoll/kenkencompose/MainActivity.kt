package com.danstoll.kenkencompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.animation.Crossfade
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.layout.Expanded
import androidx.ui.layout.Gravity
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.surface.Surface
import androidx.ui.tooling.preview.Preview
import com.danstoll.kenkencompose.data.KenKenGenerator
import com.danstoll.kenkencompose.data.boards


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        for (i in 3..8) {
            boards[i] = KenKenGenerator().generate(i)
        }
        setContent {
            MyAppTheme {
                AppContent()
            }
        }
    }

    override fun onBackPressed() {
        if(AppStatus.currentScreen is Screen.Play) {
            navigateTo(Screen.ChooseBoard)
        }
        else {
            super.onBackPressed()
        }
    }
}


@Composable
private fun AppContent() {
    Crossfade(AppStatus.currentScreen) { screen ->
        Surface(modifier = Expanded) {
            when (screen) {
                is Screen.ChooseBoard -> ChooseBoard()
                is Screen.Play -> KenKenScreen(KenKenBoardViewData.getBoard(screen.size))
            }
        }
    }
}

