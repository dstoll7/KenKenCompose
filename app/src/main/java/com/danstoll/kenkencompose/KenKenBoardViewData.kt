package com.danstoll.kenkencompose

import androidx.compose.Model
import androidx.ui.graphics.Color
import com.danstoll.kenkencompose.data.KenKenGenerator
import com.danstoll.kenkencompose.data.boards

@Model
class KenKenBoardViewData(val kenKenCells: List<List<KenKenCellViewData>>) {
    private var selectedCellIndex = Pair(0, 0)

    private var checkAnswers = false
    val checkAnswersText: String
        get() = if (checkAnswers) "Hide Answers"
        else "Check Answers"

    fun selectCell(selectedCell: KenKenCellViewData) {
        selectedCellIndex = selectedCell.index
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

        fun getBoard(size: Int): KenKenBoardViewData? {
            val squares = boards[size]?.kenKenCells?.map {
                it.map { cell ->
                    KenKenCellViewData.create(cell.answer, cell.cage, cell.index)
                }
            }
            return squares?.let { KenKenBoardViewData(it) }
        }
    }
}

@Model
data class KenKenCellViewData(
    var cellType: CellType,
    val answer: Int,
    val cage: Cage?,
    var userAnswer: Int?,
    val index: Pair<Int, Int>
) {
    companion object {
        fun create(answer: Int, cage: Cage?, index: Pair<Int, Int>) =
            KenKenCellViewData(CellType.NONE, answer, cage, null, index)
    }
}

data class Cage(
    val id: String,
    val text: String? = null,
    val adjacentCages: MutableSet<AdjacentCage>
)

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
        fun fromCell(cell: KenKenCellViewData, isSelected: Boolean, checkAnswers: Boolean) =
            when {
                isSelected -> SELECTED
                checkAnswers && cell.userAnswer == cell.answer -> CORRECT
                checkAnswers && cell.userAnswer != null && cell.userAnswer != cell.answer -> INCORRECT
                else -> NONE
            }
    }
}