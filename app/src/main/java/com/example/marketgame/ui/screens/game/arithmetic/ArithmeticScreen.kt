package com.example.marketgame.ui.screens.game.arithmetic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.marketgame.data.model.ArithmeticQuestion
import com.example.marketgame.ui.components.CompletionDialog
import com.example.marketgame.ui.components.CompletionReward
import com.example.marketgame.ui.screens.game.components.CompactGameHeader
import com.example.marketgame.ui.screens.game.components.GameBackground
import com.example.marketgame.ui.screens.game.components.PreGameCountdownOverlay
import com.example.marketgame.viewmodel.ArithmeticViewModel
import kotlinx.coroutines.delay

private const val GAME_DURATION_SECONDS = 60

@Composable
fun ArithmeticScreen(
    navController: NavController,
    viewModel: ArithmeticViewModel = viewModel()
) {
    val question: ArithmeticQuestion? = viewModel.currentQuestion

    var gameOverByTimeout by rememberSaveable(viewModel.isStarted) { mutableStateOf(false) }
    var showCompletionModal by rememberSaveable(viewModel.isStarted) { mutableStateOf(false) }
    var remainingSeconds by rememberSaveable(viewModel.isStarted) { mutableIntStateOf(GAME_DURATION_SECONDS) }
    var countdownActive by rememberSaveable(viewModel.isStarted) { mutableStateOf(true) }
    var countdownSeconds by rememberSaveable(viewModel.isStarted) { mutableIntStateOf(3) }

    val isRunning = viewModel.isStarted && question != null && !gameOverByTimeout && !countdownActive
    val timeProgress = (remainingSeconds.toFloat() / GAME_DURATION_SECONDS).coerceIn(0f, 1f)

    LaunchedEffect(countdownActive, countdownSeconds, viewModel.isStarted) {
        if (!viewModel.isStarted || !countdownActive) return@LaunchedEffect
        if (countdownSeconds <= 0) {
            countdownActive = false
            return@LaunchedEffect
        }
        delay(1_000L)
        countdownSeconds -= 1
    }

    LaunchedEffect(isRunning) {
        while (isRunning && remainingSeconds > 0) {
            delay(1_000L)
            remainingSeconds = (remainingSeconds - 1).coerceAtLeast(0)
            viewModel.onTick(GAME_DURATION_SECONDS - remainingSeconds)
            if (remainingSeconds == 0) {
                gameOverByTimeout = true
                viewModel.timeout()
            }
        }
    }

    LaunchedEffect(viewModel.isAnswerLocked) {
        if (viewModel.isAnswerLocked) {
            delay(1_000L)
            viewModel.advanceQuestion()
        }
    }

    val isGameOver = gameOverByTimeout
    val progress = if (viewModel.isStarted) timeProgress else 0f

    LaunchedEffect(viewModel.isCompleted) {
        if (viewModel.isCompleted && !isGameOver) {
            showCompletionModal = true
        }
    }
    LaunchedEffect(gameOverByTimeout) {
        if (gameOverByTimeout) {
            showCompletionModal = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GameBackground(modifier = Modifier.fillMaxSize())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
        ) {
            item {
                CompactGameHeader(
                    progress = progress,
                    lives = 1,
                    statLabel = "Skor",
                    statValue = viewModel.score.toString(),
                    instruction = "Ketuk angka lalu submit sebelum waktu habis.",
                    onExit = { navController.popBackStack() },
                    onSettings = {}
                )
            }
            if (!viewModel.isStarted) {
                item { DifficultyPickerCard(onSelect = { viewModel.startGame(it) }) }
            } else if (isGameOver) {
                item { TimeOutCard(score = viewModel.score, onReplay = { viewModel.startGame("Mudah") }, onExit = { navController.popBackStack() }) }
            } else {
                item {
                    val q = question ?: return@item
                    QuestionCard(q = q, questionNumber = viewModel.currentIndex + 1, total = viewModel.questions.size)
                }
                item {
                    AnswerInputCard(
                        input = viewModel.input,
                        locked = viewModel.isAnswerLocked,
                        feedback = viewModel.feedbackMessage,
                        onAppend = { viewModel.appendDigit(it) },
                        onClear = { viewModel.clearInput() },
                        onSubmit = { viewModel.submitAnswer() }
                    )
                }
            }
        }

        if (viewModel.isStarted && countdownActive) {
            PreGameCountdownOverlay(
                countdown = countdownSeconds,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    if (showCompletionModal) {
        CompletionDialog(
            title = if (gameOverByTimeout) "Waktu Habis" else "Arithmetic Selesai",
            message = "Skor kamu: ${viewModel.score}",
            rewards = listOf(
                CompletionReward("XP", "+50 XP"),
                CompletionReward("Hero Koin", "+20 HK")
            ),
            primaryButtonText = "Kembali ke Hub",
            secondaryButtonText = "Main Lagi",
            onPrimaryClick = {
                showCompletionModal = false
                navController.popBackStack()
            },
            onSecondaryClick = {
                showCompletionModal = false
                gameOverByTimeout = false
                remainingSeconds = GAME_DURATION_SECONDS
                countdownActive = true
                countdownSeconds = 3
                viewModel.startGame("Mudah")
            },
            onDismiss = { showCompletionModal = false },
            accentColor = Color(0xFF2E7D32)
        )
    }
}

@Composable
private fun DifficultyPickerCard(onSelect: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Pilih tingkat kesulitan",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Button(onClick = { onSelect("Mudah") }, modifier = Modifier.fillMaxWidth()) { Text(text = "Mudah") }
            Button(onClick = { onSelect("Sedang") }, modifier = Modifier.fillMaxWidth()) { Text(text = "Sedang") }
            Button(onClick = { onSelect("Sulit") }, modifier = Modifier.fillMaxWidth()) { Text(text = "Sulit") }
        }
    }
}

@Composable
private fun TimeOutCard(score: Int, onReplay: () -> Unit, onExit: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Waktu Habis", style = MaterialTheme.typography.titleMedium)
            Text(text = "Skor: $score")
            Button(onClick = onReplay, modifier = Modifier.fillMaxWidth()) { Text(text = "Main Lagi") }
            Button(onClick = onExit, modifier = Modifier.fillMaxWidth()) { Text(text = "Kembali ke Hub") }
        }
    }
}

@Composable
private fun QuestionCard(q: ArithmeticQuestion, questionNumber: Int, total: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.78f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Soal $questionNumber / $total",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = q.display,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun AnswerInputCard(
    input: String,
    locked: Boolean,
    feedback: String?,
    onAppend: (String) -> Unit,
    onClear: () -> Unit,
    onSubmit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.78f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (input.isEmpty()) "?" else input,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (input.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurface
                )
            }

            NumpadRow(listOf("1", "2", "3"), onAppend)
            NumpadRow(listOf("4", "5", "6"), onAppend)
            NumpadRow(listOf("7", "8", "9"), onAppend)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !locked
                ) { Text(text = "Hapus") }
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !locked
                ) { Text(text = "Submit") }
            }

            feedback?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    color = if (it.contains("Benar")) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )
            }
        }
    }
}

@Composable
private fun NumpadRow(digits: List<String>, onAppend: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        digits.forEach { d ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(64.dp)
                    .background(Color(0xFFE8F5E9), CircleShape)
                    .clickable { onAppend(d) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = d, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
    Spacer(modifier = Modifier.size(2.dp))
}
