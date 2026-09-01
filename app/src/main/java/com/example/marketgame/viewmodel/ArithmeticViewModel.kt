package com.example.marketgame.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketgame.data.dummy.ArithmeticDummyData
import com.example.marketgame.data.model.ArithmeticQuestion
import com.example.marketgame.data.remote.GameDataRepository
import kotlinx.coroutines.launch
import java.util.UUID

class ArithmeticViewModel : ViewModel() {

    var questions by mutableStateOf<List<ArithmeticQuestion>>(emptyList())
        private set
    var currentIndex by mutableIntStateOf(0)
        private set
    var score by mutableIntStateOf(0)
        private set
    var correctCount by mutableIntStateOf(0)
        private set
    var answeredCount by mutableIntStateOf(0)
        private set
    var input by mutableStateOf("")
        private set
    var feedbackMessage by mutableStateOf<String?>(null)
        private set
    var isAnswerLocked by mutableStateOf(false)
        private set
    var isStarted by mutableStateOf(false)
        private set
    var isCompleted by mutableStateOf(false)
        private set
    var difficulty by mutableStateOf("Mudah")
        private set
    var durationSeconds by mutableIntStateOf(0)
        private set

    var lastSessionId: String? = null
        private set

    private val submittedAnswers = mutableListOf<Int?>()

    val currentQuestion: ArithmeticQuestion?
        get() = questions.getOrNull(currentIndex)

    fun startGame(selectedDifficulty: String) {
        difficulty = selectedDifficulty
        questions = ArithmeticDummyData.generateQuestions(selectedDifficulty)
        currentIndex = 0
        score = 0
        correctCount = 0
        answeredCount = 0
        input = ""
        feedbackMessage = null
        isAnswerLocked = false
        isCompleted = false
        isStarted = true
        durationSeconds = 0
        submittedAnswers.clear()
    }

    fun appendDigit(digit: String) {
        if (isAnswerLocked) return
        if (input.length >= 5) return
        if (digit == "-" && input.isEmpty()) {
            input = "-"
        } else if (digit != "-") {
            input = input + digit
        }
    }

    fun clearInput() {
        if (!isAnswerLocked) input = ""
    }

    fun submitAnswer() {
        if (isAnswerLocked) return
        val value = input.trim()
        if (value.isEmpty() || value == "-") return
        val question = currentQuestion ?: return
        val userAnswer = value.toIntOrNull() ?: return

        // Record the user's actual submission for server-side verification.
        submittedAnswers.add(userAnswer)

        val isCorrect = userAnswer == question.answer
        if (isCorrect) {
            score += 10
            correctCount += 1
            feedbackMessage = "Benar!"
        } else {
            feedbackMessage = "Salah. Jawaban: ${question.answer}"
        }
        answeredCount += 1
        isAnswerLocked = true
    }

    fun advanceQuestion() {
        if (!isAnswerLocked) return
        currentIndex += 1
        input = ""
        feedbackMessage = null
        isAnswerLocked = false
        if (currentIndex >= questions.size) {
            isCompleted = true
            reportGame()
        }
    }

    fun timeout() {
        if (isCompleted || isAnswerLocked) return
        isCompleted = true
        reportGame()
    }

    fun onTick(seconds: Int) {
        durationSeconds = seconds
    }

    private fun reportGame() {
        val sessionId = UUID.randomUUID().toString()
        lastSessionId = sessionId
        viewModelScope.launch {
            GameDataRepository.reportArithmeticKilat(
                sessionId = sessionId,
                durationSeconds = durationSeconds,
                difficulty = difficulty,
                questions = questions,
                userAnswers = submittedAnswers
            )
        }
    }
}
