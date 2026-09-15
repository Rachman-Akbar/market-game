package com.example.marketgame.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketgame.data.model.QuizQuestion
import com.example.marketgame.data.remote.GameDataRepository
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    private var allQuestions: List<QuizQuestion> = emptyList()

    var questions by mutableStateOf<List<QuizQuestion>>(emptyList())
        private set
    var isContentLoading by mutableStateOf(true)
        private set
    var contentError by mutableStateOf<String?>(null)
        private set
    var currentIndex by mutableStateOf(0)
        private set
    var score by mutableStateOf(0)
        private set
    var correctCount by mutableStateOf(0)
        private set
    var answeredCount by mutableStateOf(0)
        private set
    var feedbackMessage by mutableStateOf<String?>(null)
        private set
    var lastExplanation by mutableStateOf<String?>(null)
        private set
    var isAnswerLocked by mutableStateOf(false)
        private set
    var isQuizStarted by mutableStateOf(false)
        private set
    var isQuizCompleted by mutableStateOf(false)
        private set

    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentIndex)

    init {
        loadContent()
    }

    fun loadContent() {
        viewModelScope.launch {
            isContentLoading = true
            contentError = null
            GameDataRepository.getQuizQuestions()
                .onSuccess { loaded ->
                    allQuestions = loaded
                    isContentLoading = false
                }
                .onFailure { e ->
                    contentError = e.message ?: "Gagal memuat soal kuis."
                    isContentLoading = false
                }
        }
    }

    fun startQuiz(difficulty: String) {
        val pool = if (allQuestions.isEmpty()) emptyList() else allQuestions
        questions = pool.filter { it.difficulty == difficulty }.shuffled()
        if (questions.isEmpty() && pool.isNotEmpty()) {
            questions = pool.shuffled()
        }
        currentIndex = 0
        score = 0
        correctCount = 0
        answeredCount = 0
        feedbackMessage = null
        lastExplanation = null
        isAnswerLocked = false
        isQuizStarted = true
    }

    fun answerQuestion(answer: String) {
        if (isAnswerLocked) return
        val question = currentQuestion ?: return
        val isCorrect = answer == question.correctAnswer

        if (isCorrect) {
            score += 10
            correctCount += 1
            feedbackMessage = "Benar!"
        } else {
            feedbackMessage = "Salah."
        }
        lastExplanation = question.explanation
        answeredCount += 1
        isAnswerLocked = true
    }

    fun advanceQuestion() {
        if (!isAnswerLocked) return
        currentIndex += 1
        feedbackMessage = null
        lastExplanation = null
        isAnswerLocked = false

        if (currentIndex >= questions.size) {
            isQuizCompleted = true
            reportGameCompletion("quiz_completed", score)
        }
    }

    private fun reportGameCompletion(eventType: String, value: Int) {
        viewModelScope.launch {
            GameDataRepository.reportGameCompletion(eventType, value)
        }
    }
}