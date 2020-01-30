package com.danstoll.kenkencompose

import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.*
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.tooling.preview.Preview
import androidx.ui.layout.Size

@Composable
fun ChooseBoard() {
    WithConstraints { constraints ->
        val containerSize = withDensity(+ambientDensity()) {
            Pair(constraints.maxWidth.toDp(), constraints.maxHeight.toDp())
        }
        val buttonPadding = 20.dp
        val buttonSize = (containerSize.first / 3) - (buttonPadding)
        Column() {
            TopAppBar(
                title = {
                    Text(
                        text = appBarText,
                        style = (+MaterialTheme.typography()).subtitle1,
                        modifier = Gravity.Center
                    )
                }
            )
            HeightSpacer(height = 30.dp)
            Text(
                text = "Choose Board Size",
                style = (+MaterialTheme.typography()).h4,
                modifier = Gravity.Center
            )
            Column(modifier = Gravity.Center) {
                ChooseBoardButtonRow(start = 3, end = 5, buttonSize = buttonSize)
                ChooseBoardButtonRow(start = 6, end = 8, buttonSize = buttonSize)
            }
        }
    }
}

@Composable
fun ChooseBoardButtonRow(start: Int, end: Int, buttonSize: Dp) {
    HeightSpacer(height = 30.dp)
    Row(modifier = ExpandedWidth, arrangement = Arrangement.SpaceEvenly) {
        for (i in start..end) {
            DrawChooseBoardButton(i, buttonSize)
        }
    }
}

@Composable
fun DrawChooseBoardButton(boardSize: Int, buttonSize:Dp) {
        Button(
            onClick = { navigateTo(Screen.Play(boardSize)) },
            modifier = Size(buttonSize, buttonSize)
        ) {
            Text(text = "$boardSize X $boardSize", style = (+MaterialTheme.typography()).h5)
        }
}

@Preview
@Composable
fun chooseBoardPreview() {
    ChooseBoard()
}