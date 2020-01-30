package com.danstoll.kenkencompose

import android.util.Log
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class KenKenGenerator {

    companion object {
        // Staring probability of unioning two constraint blocks
        private const val startingUnionProb = .7
        // Probability of using + or x (instead of - or /) for 2 cells
        private const val addOrMultTwoCellsProb = .1
        //Probability of using - (instead of /) for 2 diviable cells (given not using + # or x)
        private const val minusProb = .1
        // Probability of using x instead of + for more than 2 cells
        private const val multProb = .5
    }


    private val constraintMap = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
    private var board = mutableListOf(mutableListOf<Int>())
    private var answer = mutableListOf(mutableListOf<Int>())

    private var kenkenCells = mutableListOf(mutableListOf<KenKenCell>())
    private val cageMap = mutableMapOf<Pair<Int, String>, List<Pair<Int, Int>>>()

    fun generate(size: Int): List<List<KenKenCell>> {


        val n = size
        for (i in 0 until n) {
            val tempList = mutableListOf<Int>()
            for (j in 0 until n) {
                tempList.add((i * n + j))
            }
            board.add(i, tempList)
        }
        board = board.filterNot { it.isEmpty() }.toMutableList()

        for (i in 0 until n * n) {
            constraintMap[i] = mutableListOf(Pair(i / n, i % n))
        }

        // Randomly generate constraint blocks
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (i + 1 < n)
                    possiblyUnion(board[i][j], board[i + 1][j])
                if (j + 1 < n)
                    possiblyUnion(board[i][j], board[i][j + 1])
            }
        }

        val rowSwaps = List(n) { i -> i }.shuffled()
        val columnSwaps = List(n) { i -> i }.shuffled()
        val valueSwaps = List(n) { i -> i }.shuffled()
        Log.d("bla", rowSwaps.toString())
        Log.d("bla", columnSwaps.toString())
        Log.d("bla", valueSwaps.toString())
        for (i in 0 until n) {
            val addList = mutableListOf<Int>()
            for (j in 0 until n) {
                addList.add(-1)
            }
            answer.add(addList)
        }
        answer = answer.filterNot { it.isEmpty() }.toMutableList()

        for (i in 0 until n) {
//            answer.add(mutableListOf())
            for (j in 0 until n) {
                answer[rowSwaps[i]][columnSwaps[j]] = valueSwaps[(i + j) % n] + 1
            }
        }

        // Generate block constraint output
        val constraints = mutableListOf<String>()
        constraintMap.forEach { (key, value) ->
            if (value.isNotEmpty()) {
                val cells = value.joinToString(" ") { pair -> "${pair.first},${pair.second}" }
                val v = value.map { pair -> answer[pair.first][pair.second] }

                val type: String
                val num: Int

                if (v.size == 1) {
                    type = ""
                    num = v[0]
                } else if (v.size == 2 && Random.nextFloat() > addOrMultTwoCellsProb) {
                    if (max(v[0], v[1]) % min(v[0], v[1]) == 0 && Random.nextFloat() > minusProb) {
                        type = "/"
                        num = max(v[0], v[1]) / min(v[0], v[1])
                    } else {
                        type = "-"
                        num = abs(v[0] - v[1])
                    }
                } else if (Random.nextFloat() < multProb) {
                    type = "x"
                    num = v.fold(1) { acc, i -> acc * i }
                } else {
                    type = "+"
                    num = v.fold(0) { acc, i -> acc + i }
                }
                constraints.add("$type$num $cells")
                cageMap[Pair(key, "$num$type")] = value
            }

        }

//        for (i in 0 until size) {
//            val tempList = mutableListOf<KenKenCell?>()
//            for (j in 0 until size) {
//                tempList.add(null)
//            }
//            kenkenCells.add(tempList)
//        }


        constraints.addAll(answer.map { list -> list.joinToString(" ") })
//        return constraints.filter { it.isNotEmpty() }
        return buildKenKenBoard()
    }

    private fun possiblyUnion(x: Int, y: Int) {
        if (x == y)
            return

        // Determine if should union
        val probFactor = max(constraintMap[x]?.let { 2 } ?: 1, constraintMap[y]?.let { 2 } ?: 1)
        if (Random.nextFloat() > startingUnionProb / probFactor)
            return

        constraintMap[y]?.forEach {
            board[it.first][it.second] = x
        }
        constraintMap[y]?.let { constraintMap[x]?.addAll(it) }

        constraintMap[y] = mutableListOf()
    }

    private fun buildKenKenBoard(): List<List<KenKenCell>> {
        kenkenCells = kenkenCells.filter { it.isNotEmpty() }.toMutableList()
        answer.forEachIndexed { x, row ->
            val tempList = mutableListOf<KenKenCell>()
            row.forEachIndexed { y, cell ->
                tempList.add(KenKenCell(cell, null, Pair(x, y)))
            }
            kenkenCells.add(tempList)
        }
        cageMap.forEach { (keyPair, indices) ->
            //Get the smallest index to assign the constraint to that square
            val smallestIndex = indices.minBy { it.first + it.second }
            smallestIndex?.let {
                //Extra check to get the smallest y coordinate if multiple indices have the same sum
                val newSmallest =
                    indices.filter { (it.first + it.second) == (smallestIndex.first + smallestIndex.second) }
                        .minBy { it.second }
                indices.forEach {
                    kenkenCells[it.first][it.second]?.cage = Cage(
                        keyPair.first.toString(),
                        if (it == newSmallest) keyPair.second else null,
                        mutableSetOf()
                    )
                }
            }
        }
        kenkenCells.forEachIndexed { i, rows ->
            rows.forEachIndexed { j, kenKenCell ->
                val adjacentCages = mutableSetOf<AdjacentCage>()
                if (i > 0 && kenkenCells[i - 1][j].cage?.id == kenKenCell.cage?.id) {
                    adjacentCages.add(AdjacentCage.TOP)
                }
                if (j > 0 && kenkenCells[i][j - 1].cage?.id == kenKenCell.cage?.id) {
                    adjacentCages.add(AdjacentCage.LEFT)
                }
                if (i < kenkenCells.size - 1 && kenkenCells[i + 1][j].cage?.id == kenKenCell.cage?.id) {
                    adjacentCages.add(AdjacentCage.BOTTOM)
                }
                if (j < kenkenCells.size - 1 && kenkenCells[i][j + 1].cage?.id == kenKenCell.cage?.id) {
                    adjacentCages.add(AdjacentCage.RIGHT)
                }
                kenKenCell.cage?.adjacentCages?.addAll(adjacentCages)
            }
        }
        return kenkenCells
    }
}

data class KenKenCell(
    val answer: Int,
    var cage: Cage?,
    val index: Pair<Int, Int>
)

