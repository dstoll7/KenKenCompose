package com.danstoll.kenkencompose

import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.*
import androidx.ui.engine.geometry.Offset
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Paint
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.ContainedButtonStyle
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.ripple.Ripple
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.layout.Size
import androidx.ui.tooling.preview.Preview
import com.danstoll.kenkencompose.data.KenKenGenerator
import com.danstoll.kenkencompose.data.boards

@Composable
fun KenKenScreen(board: KenKenBoardViewData? = KenKenBoardViewData.getBoard(4)) {

    WithConstraints { constraints ->
        val containerWidth = withDensity(+ambientDensity()) {
            constraints.maxWidth.toDp()
        }
        Column(modifier = ExpandedHeight) {
            TopAppBar(
                    title = {
                        Text(
                                text = appBarText,
                                style = (+MaterialTheme.typography()).subtitle1,
                                modifier = Gravity.Center
                        )
                    },
                    navigationIcon = {
                        VectorImageButton(R.drawable.ic_back) {
                            navigateTo(Screen.ChooseBoard)
                        }
                    }
            )
            //Board should never be null, but handle with basic error text
            if (board == null) {
                val containerHeight = withDensity(+ambientDensity()) {
                    constraints.maxHeight.toDp()
                }
                HeightSpacer(height = containerHeight / 2)

                Text(text = "Error Loading Boards", style = TextStyle(fontSize = 30.sp, color = Color.Black), modifier = Gravity.Center)
            } else {


                val boardSize = board.kenKenCells.size
                val cellDpSize = (containerWidth / boardSize)

                Column(modifier = ExpandedHeight, arrangement = Arrangement.SpaceAround) {

                    CheckAnswersButton(board)
                    DrawBoard(board, boardSize, cellDpSize)
                    DrawNumberButtons(board, boardSize, cellDpSize, containerWidth / boardSize)
                    DrawClearButtons(board)
                }
            }
        }
    }
}

@Composable
fun CheckAnswersButton(board: KenKenBoardViewData) {
    Row(modifier = ExpandedWidth, arrangement = Arrangement.SpaceEvenly) {
        Button(
                text = board.checkAnswersText,
                onClick = { board.toggleCheckAnswers() },
                style = ContainedButtonStyle()
        )
    }
}

@Composable
fun DrawNumberButtons(board: KenKenBoardViewData, boardSize: Int, cellDpSize: Dp, maxWidth: Dp) {
    val textSize = (cellDpSize.value * 0.3f).sp
    Row(modifier = ExpandedWidth, arrangement = Arrangement.SpaceEvenly) {
        for (i in 1..boardSize) {
            Button(
                    onClick = { board.userInput(i) },
                    style = ContainedButtonStyle(),
                    modifier = MaxWidth(maxWidth)
            )
            {
                Text(text = i.toString(), style = TextStyle(fontSize = textSize))
            }
        }
    }
}

@Composable
fun DrawClearButtons(board: KenKenBoardViewData) {
    Row(modifier = ExpandedWidth, arrangement = Arrangement.SpaceEvenly) {
        Button(
                text = "Clear Cell",
                onClick = { board.clearCurrentCell() },
                style = ContainedButtonStyle()
        )
        Button(
                text = "Clear Board",
                onClick = { board.clearBoard() },
                style = ContainedButtonStyle()
        )
    }
}

@Composable
fun DrawBoard(board: KenKenBoardViewData, boardSize: Int, cellDpSize: Dp) {
    Table(columns = boardSize) {
        for (i in 0 until boardSize) {
            tableRow {
                for (j in 0 until boardSize) {
                    val kenKenCell = board.kenKenCells[i][j].apply {
                        cellType =
                                CellType.fromCell(this, index == board.getCurrentCellIndex(), board.shouldCheckAnswers())
                    }
                    DrawCell(
                            size = cellDpSize,
                            kenCell = kenKenCell
                    ) {
                        board.selectCell(kenKenCell)
                    }
                }
            }
        }
    }
}

@Composable
fun DrawCell(size: Dp, kenCell: KenKenCellViewData, onClick: (KenKenCellViewData) -> Unit) {
    val textSize = (size.value * 0.4f).sp
    val operationSize = (size.value * 0.15f).sp
    Clickable(onClick = { onClick(kenCell) }) {
        Stack {
            aligned(Alignment.Center) {
                Container(height = size, width = size) {

                    DrawShape(
                            shape = RectangleShape,
                            color = kenCell.cellType.color
                    )
                    DrawCellBorder(adjacentCages = kenCell.cage?.adjacentCages ?: emptySet())
                }
            }
            aligned(Alignment.Center) {
                kenCell.userAnswer?.toString()?.let {
                    Text(text = it, style = TextStyle(fontSize = textSize))
                }
            }

            kenCell.cage?.text?.let {
                aligned(Alignment.TopLeft) {
                    Padding(5.dp) {
                        Text(text = it, style = TextStyle(fontSize = operationSize))
                    }
                }
            }
        }
    }

}

@Composable
fun DrawCellBorder(
        color: Color = Color.Black,
        strokeWidth: Int = 3,
        adjacentCages: Set<AdjacentCage>
) {
    DrawTopBorder(
            color = color,
            strokeWidth = if (adjacentCages.contains(AdjacentCage.TOP)) 1 else strokeWidth
    )
    DrawBottomBorder(
            color = color,
            strokeWidth = if (adjacentCages.contains(AdjacentCage.BOTTOM)) 1 else strokeWidth
    )
    DrawRightBorder(
            color = color,
            strokeWidth = if (adjacentCages.contains(AdjacentCage.RIGHT)) 1 else strokeWidth
    )
    DrawLeftBorder(
            color = color,
            strokeWidth = if (adjacentCages.contains(AdjacentCage.LEFT)) 1 else strokeWidth
    )
}

@Composable
fun DrawTopBorder(color: Color, strokeWidth: Int) {
    val paint = getBorderPaint(color, strokeWidth)
    Draw { canvas, parentSize ->
        canvas.drawLine(
                Offset(0f, 0f),
                Offset(parentSize.width.value, 0f),
                paint
        )
    }
}

@Composable
fun DrawBottomBorder(color: Color, strokeWidth: Int) {
    val paint = getBorderPaint(color, strokeWidth)
    Draw { canvas, parentSize ->
        canvas.drawLine(
                Offset(0f, parentSize.height.value),
                Offset(parentSize.width.value, parentSize.height.value),
                paint
        )
    }
}

@Composable
fun DrawLeftBorder(color: Color, strokeWidth: Int) {
    val paint = getBorderPaint(color, strokeWidth)
    Draw { canvas, parentSize ->
        canvas.drawLine(
                Offset(0f, 0f),
                Offset(0f, parentSize.height.value),
                paint
        )
    }
}

@Composable
fun DrawRightBorder(color: Color, strokeWidth: Int) {
    val paint = getBorderPaint(color, strokeWidth)
    Draw { canvas, parentSize ->
        canvas.drawLine(
                Offset(parentSize.width.value, 0f),
                Offset(parentSize.width.value, parentSize.height.value),
                paint
        )
    }
}

@Composable
fun getBorderPaint(color: Color, strokeWidth: Int): Paint = Paint().apply {
    this.color = color
    this.strokeWidth = withDensity(+ambientDensity()) {
        strokeWidth.dp.toPx().value
    }
}

@Composable
fun VectorImageButton(@DrawableRes id: Int, onClick: () -> Unit) {
    Ripple(bounded = false) {
        Clickable(onClick = onClick) {
            VectorImage(id = id)
        }
    }
}

@Composable
fun VectorImage(modifier: Modifier = Modifier.None, @DrawableRes id: Int, tint: Color = Color.Transparent) {
    val vector = +vectorResource(id)
    WithDensity {
        Container(
                modifier = modifier wraps Size(vector.defaultWidth.toDp(), vector.defaultHeight.toDp())
        ) {
            DrawVector(vector, tint)
        }
    }
}

@Preview
@Composable
fun kenKenScreenPreview() {
    val size = 5
    boards[size] = KenKenGenerator().generate(size)
    KenKenScreen(KenKenBoardViewData.getBoard(size))
}