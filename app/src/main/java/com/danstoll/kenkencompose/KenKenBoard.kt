package com.danstoll.kenkencompose

import androidx.compose.Model

@Model
class KenKenBoard(val kenKenCells: Array<Array<KenKenCell>>) {
    private var selectedCellIndex = Pair(0, 0)

    fun selectCell(selectedCell: KenKenCell) {
        kenKenCells.forEachIndexed { x, arrayOfCells ->
            arrayOfCells.forEachIndexed { y, cell ->
                if (selectedCell == cell) {
                    selectedCellIndex = Pair(x, y)
                    return
                }
            }
        }
    }

    fun getCurrentCellIndex() = selectedCellIndex

    companion object {

        fun generateBoard(): KenKenBoard {
            val squares = arrayOf(
                arrayOf(
                    KenKenCell.create(1, Cage("a", "4+", setOf(AdjacentCage.RIGHT))),
                    KenKenCell.create(3, Cage("a", null, setOf(AdjacentCage.LEFT))),
                    KenKenCell.create(2, Cage("b", "3+", setOf(AdjacentCage.BOTTOM))),
                    KenKenCell.create(4, Cage("c", "4", setOf()))
                ),
                arrayOf(
                    KenKenCell.create(3, Cage("d", "7+", setOf(AdjacentCage.RIGHT))),
                    KenKenCell.create(4, Cage("d", null, setOf(AdjacentCage.LEFT))),
                    KenKenCell.create(1, Cage("b", null, setOf(AdjacentCage.TOP))),
                    KenKenCell.create(2, Cage("e", "3+", setOf(AdjacentCage.BOTTOM)))
                ),
                arrayOf(
                    KenKenCell.create(4, Cage("f", "6+", setOf(AdjacentCage.BOTTOM))),
                    KenKenCell.create(2, Cage("g", "3+", setOf(AdjacentCage.BOTTOM))),
                    KenKenCell.create(3, Cage("h", "3", setOf())),
                    KenKenCell.create(1, Cage("e", null, setOf(AdjacentCage.TOP)))
                ),
                arrayOf(
                    KenKenCell.create(2, Cage("f", null, setOf(AdjacentCage.TOP))),
                    KenKenCell.create(1, Cage("g", null, setOf(AdjacentCage.TOP))),
                    KenKenCell.create(4, Cage("i", "7+", setOf(AdjacentCage.RIGHT))),
                    KenKenCell.create(3, Cage("i", null, setOf(AdjacentCage.LEFT)))
                )
            )
            return KenKenBoard(squares)
        }
    }
}

@Model
class KenKenCell(
    var selected: Boolean = false,
    val answer: Int,
    val cage: Cage,
    var userAnswer: Int? = null
) {
    companion object {
        fun create(answer: Int, cage: Cage) =
            KenKenCell(false, answer, cage, null)
    }
}

class Cage(val id: String, val text: String? = null, val adjacentCages: Set<AdjacentCage>)

enum class AdjacentCage {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM
}