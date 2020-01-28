package com.danstoll.kenkencompose

import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.*
import androidx.ui.engine.geometry.Offset
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Paint
import androidx.ui.layout.Container
import androidx.ui.layout.Padding
import androidx.ui.layout.Stack
import androidx.ui.layout.Table
import androidx.ui.text.TextStyle
import androidx.ui.tooling.preview.Preview

@Composable
fun Grid(board: KenKenBoard = KenKenBoard.generateBoard()) {
    WithConstraints { constraints ->
        val containerWidth = withDensity(+ambientDensity()) {
            constraints.maxWidth.toDp()
        }
        val size = board.kenKenCells.size
        val squareWidth = (containerWidth / size)

        val selectedIndex = board.getCurrentCellIndex()

        Table(columns = size) {
            for (i in 0 until size) {
                tableRow {
                    for (j in 0 until size) {
                        val kenKenCell = board.kenKenCells[i][j].apply {
                            selected = (selectedIndex.first == i && selectedIndex.second == j)
                        }
                        Cell(
                            size = squareWidth,
                            kenCell = kenKenCell
                        ) {
                            board.selectCell(kenKenCell)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Cell(size: Dp, kenCell: KenKenCell, onClick: (KenKenCell) -> Unit) {
    val textSize = (size.value * 0.4f).sp
    val operationSize = (size.value * 0.15f).sp
    Clickable(onClick = { onClick(kenCell) }) {
        Stack {
            aligned(Alignment.Center) {
                Container(height = size, width = size) {

                    DrawShape(
                        shape = RectangleShape,
                        color = if (kenCell.selected) Color.Blue else Color.White
                    )
                    DrawKenKenBorder(adjacentCages = kenCell.cage.adjacentCages)
                }
            }
            aligned(Alignment.Center) {
                Text(text = kenCell.answer.toString(), style = TextStyle(fontSize = textSize))
            }

            kenCell.cage.text?.let {
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
fun DrawKenKenBorder(color: Color = Color.Black, strokeWidth: Int = 3, adjacentCages: Set<AdjacentCage>) {
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
    val paint = Paint()
    paint.color = color
    paint.strokeWidth = withDensity(+ambientDensity()) {
        strokeWidth.dp.toPx().value
    }
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
    val paint = Paint()
    paint.color = color
    paint.strokeWidth = withDensity(+ambientDensity()) {
        strokeWidth.dp.toPx().value
    }
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
    val paint = Paint()
    paint.color = color
    paint.strokeWidth = withDensity(+ambientDensity()) {
        strokeWidth.dp.toPx().value
    }
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
    val paint = Paint()
    paint.color = color
    paint.strokeWidth = withDensity(+ambientDensity()) {
        strokeWidth.dp.toPx().value
    }
    Draw { canvas, parentSize ->
        canvas.drawLine(
            Offset(parentSize.width.value, 0f),
            Offset(parentSize.width.value, parentSize.height.value),
            paint
        )
    }
}

//caused crash
@Composable
fun getBorderPaint(color: Color, strokeWidth: Int): Paint = Paint().apply {
    this.color = color
    this.strokeWidth = withDensity(+ambientDensity()) {
        strokeWidth.dp.toPx().value
    }
}

@Preview("MyScreen preview")
@Composable
fun DefaultPreview() {
    MyAppTheme {
        Grid()
    }
}