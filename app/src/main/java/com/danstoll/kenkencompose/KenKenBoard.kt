package com.danstoll.kenkencompose

import androidx.compose.Model
import androidx.ui.graphics.Color

@Model
class KenKenBoard(val kenKenCells: Array<Array<KenKenCell>>) {
    private var selectedCellIndex = Pair(0, 0)

    private var checkAnswers = false
    val checkAnswersText: String
        get() = if (checkAnswers) "Hide Answers"
        else "Check Answers"

    fun selectCell(selectedCell: KenKenCell) {
        selectedCellIndex = selectedCell.index
//        kenKenCells.forEachIndexed { x, arrayOfCells ->
//            arrayOfCells.forEachIndexed { y, cell ->
//                if (selectedCell == cell) {
//                    selectedCellIndex = Pair(x, y)
//                    return
//                }
//            }
//        }
    }

    fun userInput(number: Int) {
        kenKenCells[selectedCellIndex.first][selectedCellIndex.second].userAnswer = number
    }

    fun clearCurrentCell() {
        kenKenCells[selectedCellIndex.first][selectedCellIndex.second].userAnswer = null
    }

    fun clearBoard() {
        kenKenCells.forEach { row ->
            row.forEach { cell ->
                cell.userAnswer = null
            }
        }
    }

    fun toggleCheckAnswers() {
        checkAnswers = !checkAnswers
    }

    fun shouldCheckAnswers() = checkAnswers

    fun getCurrentCellIndex() = selectedCellIndex

    companion object {

        fun generateBoard(): KenKenBoard {
            val squares = arrayOf(
                arrayOf(
                    KenKenCell.create(1, Cage("a", "4+", setOf(AdjacentCage.RIGHT)), Pair(0, 0)),
                    KenKenCell.create(3, Cage("a", null, setOf(AdjacentCage.LEFT)), Pair(0, 1)),
                    KenKenCell.create(2, Cage("b", "3+", setOf(AdjacentCage.BOTTOM)), Pair(0, 2)),
                    KenKenCell.create(4, Cage("c", "4", setOf()), Pair(0, 3))
                ),
                arrayOf(
                    KenKenCell.create(3, Cage("d", "7+", setOf(AdjacentCage.RIGHT)), Pair(1, 0)),
                    KenKenCell.create(4, Cage("d", null, setOf(AdjacentCage.LEFT)), Pair(1, 1)),
                    KenKenCell.create(1, Cage("b", null, setOf(AdjacentCage.TOP)), Pair(1, 2)),
                    KenKenCell.create(2, Cage("e", "3+", setOf(AdjacentCage.BOTTOM)), Pair(1, 3))
                ),
                arrayOf(
                    KenKenCell.create(4, Cage("f", "6+", setOf(AdjacentCage.BOTTOM)), Pair(2, 0)),
                    KenKenCell.create(2, Cage("g", "3+", setOf(AdjacentCage.BOTTOM)), Pair(2, 1)),
                    KenKenCell.create(3, Cage("h", "3", setOf()), Pair(2, 2)),
                    KenKenCell.create(1, Cage("e", null, setOf(AdjacentCage.TOP)), Pair(2, 3))
                ),
                arrayOf(
                    KenKenCell.create(2, Cage("f", null, setOf(AdjacentCage.TOP)), Pair(3, 0)),
                    KenKenCell.create(1, Cage("g", null, setOf(AdjacentCage.TOP)), Pair(3, 1)),
                    KenKenCell.create(4, Cage("i", "7+", setOf(AdjacentCage.RIGHT)), Pair(3, 2)),
                    KenKenCell.create(3, Cage("i", null, setOf(AdjacentCage.LEFT)), Pair(3, 3))
                )
            )
            return KenKenBoard(squares)
        }
    }
}

@Model
class KenKenCell(
    var cellType: CellType,
    val answer: Int,
    val cage: Cage,
    var userAnswer: Int?,
    val index: Pair<Int, Int>
) {
    companion object {
        fun create(answer: Int, cage: Cage, index: Pair<Int, Int>) =
            KenKenCell(CellType.NONE, answer, cage, null, index)
    }
}

class Cage(val id: String, val text: String? = null, val adjacentCages: Set<AdjacentCage>)

enum class AdjacentCage {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM
}

enum class CellType(val color: Color) {
    SELECTED(Color.Yellow),
    INCORRECT(Color.Red),
    CORRECT(Color.Green),
    NONE(Color.White);

    companion object {
        fun fromCell(cell: KenKenCell, isSelected: Boolean, checkAnswers: Boolean) =
            when {
                isSelected -> SELECTED
                checkAnswers && cell.userAnswer == cell.answer -> CORRECT
                checkAnswers && cell.userAnswer != null && cell.userAnswer != cell.answer -> INCORRECT
                else -> NONE
            }
    }
}