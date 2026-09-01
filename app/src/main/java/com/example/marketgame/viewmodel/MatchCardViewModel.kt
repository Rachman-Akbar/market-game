package com.example.marketgame.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketgame.data.dummy.SdgDummyData
import com.example.marketgame.data.model.SdgGoal
import com.example.marketgame.data.model.SdgStatement
import com.example.marketgame.data.remote.GameDataRepository
import kotlinx.coroutines.launch

class MatchCardViewModel : ViewModel() {
    private var statements: List<SdgStatement> = SdgDummyData.statements.shuffled()

    val goals: List<SdgGoal> = SdgDummyData.goals

    var currentIndex by mutableIntStateOf(0)
        private set

    var score by mutableIntStateOf(0)
        private set

    var correctCount by mutableIntStateOf(0)
        private set

    var wrongCount by mutableIntStateOf(0)
        private set

    var lives by mutableIntStateOf(3)
        private set

    var lastMoveMatched by mutableStateOf<Boolean?>(null)
        private set

    var isCompleted by mutableStateOf(false)
        private set

    var isGameOver by mutableStateOf(false)
        private set

    val currentStatement: SdgStatement?
        get() = statements.getOrNull(currentIndex)

    fun selectGoal(goalId: Int) {
        if (isCompleted || isGameOver) return
        val statement = currentStatement ?: return
        val isCorrect = statement.goalId == goalId

        if (isCorrect) {
            score += 10
            correctCount += 1
            lastMoveMatched = true
        } else {
            score = (score - 5).coerceAtLeast(0)
            wrongCount += 1
            lives = (lives - 1).coerceAtLeast(0)
            lastMoveMatched = false
            if (lives <= 0) {
                isGameOver = true
                return
            }
        }

        currentIndex += 1
        if (currentIndex >= statements.size) {
            isCompleted = true
            isGameOver = true
            reportGameCompletion("match_card_completed", score)
        }
    }

    fun onTimeout() {
        if (!isCompleted && !isGameOver) {
            isGameOver = true
            lastMoveMatched = false
        }
    }

    fun resetGame() {
        statements = SdgDummyData.statements.shuffled()
        currentIndex = 0
        score = 0
        correctCount = 0
        wrongCount = 0
        lives = 3
        lastMoveMatched = null
        isCompleted = false
        isGameOver = false
    }

    private fun reportGameCompletion(eventType: String, value: Int) {
        viewModelScope.launch {
            GameDataRepository.reportGameCompletion(eventType, value)
        }
    }
}
