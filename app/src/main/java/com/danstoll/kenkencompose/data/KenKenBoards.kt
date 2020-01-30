package com.danstoll.kenkencompose.data

import com.danstoll.kenkencompose.Cage
import com.danstoll.kenkencompose.KenKenCellViewData

val boards= mutableMapOf<Int, KenKenBoard>()

data class KenKenBoard(val kenKenCells: List<List<KenKenCell>>)

data class KenKenCell(
    val answer: Int,
    var cage: Cage?,
    val index: Pair<Int, Int>
)