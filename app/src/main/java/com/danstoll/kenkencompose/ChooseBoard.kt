package com.danstoll.kenkencompose

import androidx.compose.Composable
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.material.Button

@Composable
fun ChooseBoard() {
    Column(arrangement = Arrangement.SpaceAround) {
        for (i in 3..8) {
            DrawChooseBoardButton(size = i)
        }
    }
}

@Composable
fun DrawChooseBoardButton(size: Int) {
    Button(
        text = "$size X $size",
        onClick = { navigateTo(Screen.Play(size)) }
    )
}