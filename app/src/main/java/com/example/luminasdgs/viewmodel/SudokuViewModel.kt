package com.example.luminasdgs.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luminasdgs.data.dummy.SudokuDummyData
import com.example.luminasdgs.data.remote.GameDataRepository
import kotlinx.coroutines.launch
import java.util.UUID

class SudokuViewModel : ViewModel() {

    var puzzle by mutableStateOf<List<Int>>(emptyList())
        private set
    var board by mutableStateOf<List<Int>>(emptyList())
        private set
    var selectedIndex by mutableIntStateOf(-1)
        private set
    var difficulty by mutableStateOf("Mudah")
        private set
    var isStarted by mutableStateOf(false)
        private set
    var isCompleted by mutableStateOf(false)
        private set
    var durationSeconds by mutableIntStateOf(0)
        private set
    var message by mutableStateOf<String?>(null)
        private set

    var lastSessionId: String? = null
        private set

    var givenCells by mutableStateOf<Set<Int>>(emptySet())
        private set

    val filledCount: Int
        get() = board.count { it != 0 }

    fun startGame(selectedDifficulty: String) {
        difficulty = selectedDifficulty
        val (solution, puzzleGenerated) = SudokuDummyData.generatePuzzle(selectedDifficulty)
        // solution is kept locally for validation; board is what the user edits.
        puzzle = puzzleGenerated
        board = puzzleGenerated.toMutableList()
        givenCells = mutableSetOf<Int>()
        puzzleGenerated.forEachIndexed { i, v -> if (v != 0) givenCells = givenCells + i }
        selectedIndex = -1
        isStarted = true
        isCompleted = false
        durationSeconds = 0
        message = null
    }

    fun selectCell(index: Int) {
        if (index !in board.indices) return
        if (index in givenCells) return
        selectedIndex = index
        message = null
    }

    fun inputNumber(value: Int) {
        if (selectedIndex < 0) return
        if (selectedIndex in givenCells) return
        val newBoard = board.toMutableList()
        newBoard[selectedIndex] = value
        board = newBoard
        message = null
    }

    fun clearSelected() {
        if (selectedIndex < 0) return
        if (selectedIndex in givenCells) return
        val newBoard = board.toMutableList()
        newBoard[selectedIndex] = 0
        board = newBoard
        message = null
    }

    fun onTick(seconds: Int) {
        durationSeconds = seconds
    }

    fun submit() {
        if (isCompleted) return
        val completed = board.all { it != 0 }
        if (!completed) {
            message = "Lengkapi semua sel terlebih dahulu."
            return
        }
        if (!SudokuDummyData.isValidSolution(board)) {
            message = "Solusi tidak valid. Periksa kembali baris, kolom, dan kotak."
            return
        }
        isCompleted = true
        reportGame()
    }

    private fun reportGame() {
        val sessionId = UUID.randomUUID().toString()
        lastSessionId = sessionId
        viewModelScope.launch {
            GameDataRepository.reportSudoku(
                sessionId = sessionId,
                durationSeconds = durationSeconds,
                difficulty = difficulty,
                grid = board
            )
        }
    }
}
