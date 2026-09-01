package com.example.marketgame.data.dummy

import kotlin.random.Random

object SudokuDummyData {

    fun generatePuzzle(difficulty: String): Pair<List<Int>, List<Int>> {
        val solution = generateSolvedGrid()
        val clues = when (difficulty) {
            "Mudah" -> 44
            "Sedang" -> 38
            else -> 32
        }
        val puzzle = solution.toMutableList()
        val positions = (0..80).shuffled()
        var removed = 0
        for (pos in positions) {
            if (removed >= 81 - clues) break
            val backup = puzzle[pos]
            puzzle[pos] = 0
            // keep the puzzle uniquely solvable-ish by not breaking the solution;
            // a simple uniqueness check would be heavy, so we guarantee the
            // original solution remains valid for the player to fill.
            removed++
        }
        return solution to puzzle
    }

    private fun generateSolvedGrid(): List<Int> {
        val grid = IntArray(81)
        fill(0, grid)
        return grid.toList()
    }

    private fun fill(index: Int, grid: IntArray): Boolean {
        if (index == 81) return true
        val candidates = (1..9).shuffled(Random.Default)
        for (v in candidates) {
            if (isSafe(index, v, grid)) {
                grid[index] = v
                if (fill(index + 1, grid)) return true
                grid[index] = 0
            }
        }
        return false
    }

    private fun isSafe(index: Int, value: Int, grid: IntArray): Boolean {
        val row = index / 9
        val col = index % 9
        for (c in 0 until 9) if (grid[row * 9 + c] == value) return false
        for (r in 0 until 9) if (grid[r * 9 + col] == value) return false
        val startRow = row / 3 * 3
        val startCol = col / 3 * 3
        for (r in startRow until startRow + 3) {
            for (c in startCol until startCol + 3) {
                if (grid[r * 9 + c] == value) return false
            }
        }
        return true
    }

    fun isValidSolution(grid: List<Int>): Boolean {
        if (grid.size != 81) return false
        for (v in grid) if (v < 1 || v > 9) return false
        for (r in 0 until 9) {
            val seen = BooleanArray(10)
            for (c in 0 until 9) {
                val v = grid[r * 9 + c]
                if (seen[v]) return false
                seen[v] = true
            }
        }
        for (c in 0 until 9) {
            val seen = BooleanArray(10)
            for (r in 0 until 9) {
                val v = grid[r * 9 + c]
                if (seen[v]) return false
                seen[v] = true
            }
        }
        for (br in 0 until 3) {
            for (bc in 0 until 3) {
                val seen = BooleanArray(10)
                for (r in br * 3 until br * 3 + 3) {
                    for (c in bc * 3 until bc * 3 + 3) {
                        val v = grid[r * 9 + c]
                        if (seen[v]) return false
                        seen[v] = true
                    }
                }
            }
        }
        return true
    }
}
