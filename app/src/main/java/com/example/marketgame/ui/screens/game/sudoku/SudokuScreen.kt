package com.example.marketgame.ui.screens.game.sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.marketgame.ui.components.CompletionDialog
import com.example.marketgame.ui.components.CompletionReward
import com.example.marketgame.ui.screens.game.components.CompactGameHeader
import com.example.marketgame.ui.screens.game.components.GameBackground
import com.example.marketgame.ui.screens.game.components.PreGameCountdownOverlay
import com.example.marketgame.viewmodel.SudokuViewModel
import kotlinx.coroutines.delay

private const val GAME_DURATION_SECONDS = 600

@Composable
fun SudokuScreen(
    navController: NavController,
    viewModel: SudokuViewModel = viewModel()
) {
    var remainingSeconds by rememberSaveable(viewModel.isStarted) { mutableIntStateOf(GAME_DURATION_SECONDS) }
    var countdownActive by rememberSaveable(viewModel.isStarted) { mutableStateOf(true) }
    var countdownSeconds by rememberSaveable(viewModel.isStarted) { mutableIntStateOf(3) }
    var gameOverByTimeout by rememberSaveable(viewModel.isStarted) { mutableStateOf(false) }
    var showCompletionModal by rememberSaveable(viewModel.isStarted) { mutableStateOf(false) }

    val isRunning = viewModel.isStarted && !viewModel.isCompleted && !gameOverByTimeout && !countdownActive
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
                viewModel.onTimeout()
            }
        }
    }

    LaunchedEffect(viewModel.isCompleted) {
        if (viewModel.isCompleted && !gameOverByTimeout) {
            showCompletionModal = true
        }
    }

    var messageText by rememberSaveable { mutableStateOf<String?>(null) }
    LaunchedEffect(viewModel.message) {
        messageText = viewModel.message
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
                    progress = timeProgress,
                    lives = 1,
                    statLabel = "Terisi",
                    statValue = "${viewModel.filledCount}/81",
                    instruction = "Isi 1-9 tanpa berulang per baris, kolom, dan kotak 3x3.",
                    onExit = { navController.popBackStack() },
                    onSettings = {}
                )
            }
            if (!viewModel.isStarted) {
                item { SudokuDifficultyCard(onSelect = { viewModel.startGame(it) }) }
            } else if (gameOverByTimeout) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = "Waktu Habis", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Terisi: ${viewModel.filledCount}/81")
                            Button(onClick = { viewModel.startGame("Mudah") }, modifier = Modifier.fillMaxWidth()) { Text(text = "Main Lagi") }
                            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text(text = "Kembali ke Hub") }
                        }
                    }
                }
            } else {
                if (viewModel.board.isNotEmpty()) {
                    item { SudokuGrid(viewModel = viewModel) }
                    item {
                        SudokuNumberPad(
                            onNumber = { viewModel.inputNumber(it) },
                            onClear = { viewModel.clearSelected() }
                        )
                    }
                    item {
                        Button(
                            onClick = { viewModel.submit() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(999.dp)
                        ) { Text(text = "Submit Solusi") }
                    }
                    messageText?.let {
                        item {
                            Text(
                                text = it,
                                color = if (it.contains("invalid") || it.contains("Lengkapi") || it.contains("Waktu")) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
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
            title = "Sudoku Selesai",
            message = "Selamat, kamu menyelesaikan puzzle!",
            rewards = listOf(
                CompletionReward("XP", "+80 XP"),
                CompletionReward("Hero Koin", "+50 HK")
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
            accentColor = Color(0xFF455A64)
        )
    }
}

@Composable
private fun SudokuDifficultyCard(onSelect: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Pilih tingkat kesulitan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Button(onClick = { onSelect("Mudah") }, modifier = Modifier.fillMaxWidth()) { Text(text = "Mudah") }
            Button(onClick = { onSelect("Sedang") }, modifier = Modifier.fillMaxWidth()) { Text(text = "Sedang") }
            Button(onClick = { onSelect("Sulit") }, modifier = Modifier.fillMaxWidth()) { Text(text = "Sulit") }
        }
    }
}

@Composable
private fun SudokuGrid(viewModel: SudokuViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        val board = viewModel.board
        val given = viewModel.givenCells
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            for (r in 0..8) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (c in 0..8) {
                        val index = r * 9 + c
                        val value = board.getOrNull(index)
                        val isGiven = index in given
                        val isSelected = viewModel.selectedIndex == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(
                                    when {
                                        isSelected -> Color(0xFFB2DFDB)
                                        (r / 3 + c / 3) % 2 == 0 -> Color(0xFFF4FBF6)
                                        else -> Color(0xFFE8F5E9)
                                    },
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable { viewModel.selectCell(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (value != 0) {
                                Text(
                                    text = value.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = if (isGiven) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isGiven) MaterialTheme.colorScheme.onSurface else Color(0xFF0288D1)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SudokuNumberPad(
    onNumber: (Int) -> Unit,
    onClear: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.78f))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..5).forEach { n ->
                    NumberKey(n, Modifier.weight(1f), onNumber)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (6..9).forEach { n ->
                    NumberKey(n, Modifier.weight(1f), onNumber)
                }
                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(10.dp)
                ) { Text(text = "✕") }
            }
            Text(
                text = "Ketuk sel yang kosong lalu pilih angka.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun NumberKey(value: Int, modifier: Modifier, onNumber: (Int) -> Unit) {
    Box(
        modifier = modifier
            .height(46.dp)
            .padding(horizontal = 2.dp)
            .background(Color(0xFFE8F5E9), RoundedCornerShape(10.dp))
            .clickable { onNumber(value) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = value.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
