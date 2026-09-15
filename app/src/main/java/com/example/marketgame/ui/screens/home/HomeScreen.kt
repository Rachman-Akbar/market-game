package com.example.marketgame.ui.screens.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.marketgame.data.model.DailyMission
import com.example.marketgame.navigation.Screen
import com.example.marketgame.ui.theme.BluePrimary
import com.example.marketgame.ui.theme.GreenPrimary
import com.example.marketgame.ui.theme.YellowAccent
import com.example.marketgame.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val vm = viewModel

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 32.dp)
        ) {
            item {
                HomeHeader(
                    initials = vm.userInitials,
                    name = vm.userName,
                    email = vm.userEmail,
                    onOpenSettings = { vm.openSettings() }
                )
            }

            item {
                HeroPlayCard(
                    userName = vm.userName,
                    completedMissions = vm.completedMissionsToday,
                    totalMissions = vm.missions.size,
                    onPlay = { vm.openGameMenu() },
                    onOpenMissions = { vm.openMissions() }
                )
            }

            item {
                StatsRow(
                    gamesPlayed = vm.gameSummary?.games_played ?: 0,
                    coinsEarned = vm.gameSummary?.coins_earned ?: 0,
                    missionsDone = vm.completedMissionsToday
                )
            }

            item {
                DailyMissionsPreview(
                    missions = vm.missions,
                    onOpenMissions = { vm.openMissions() }
                )
            }

            item {
                GamesSection(onPlay = { vm.openGameMenu() })
            }
        }

        if (vm.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (vm.showGameMenuModal) {
            ChooseGameDialog(
                onDismiss = { vm.closeGameMenu() },
                onSelect = { route ->
                    vm.closeGameMenu()
                    navController.navigate(route)
                }
            )
        }

        if (vm.showMissionsModal) {
            MissionsDialog(
                missions = vm.missions,
                onDismiss = { vm.closeMissions() }
            )
        }

        if (vm.showSettingsModal) {
            SettingsDialog(
                name = vm.userName,
                email = vm.userEmail,
                onDismiss = { vm.closeSettings() },
                onOpenProfile = { vm.closeSettings(); navController.navigate(Screen.Profile.route) },
                onOpenAchievements = { vm.closeSettings(); navController.navigate(Screen.AchievementHub.route) },
                onOpenVouchers = { vm.closeSettings(); navController.navigate(Screen.Vouchers.route) },
                onOpenApiSettings = { vm.closeSettings(); navController.navigate(Screen.ApiSettings.route) },
                onLogout = {
                    vm.logout {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeHeader(
    initials: String,
    name: String,
    email: String,
    onOpenSettings: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFB7EFC5), GreenPrimary))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Halo, $name!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (email.isNotBlank()) {
                    Text(
                        text = email,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 3.dp,
            modifier = Modifier.clickable { onOpenSettings() }
        ) {
            IconButton(onClick = onOpenSettings) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Pengaturan",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun HeroPlayCard(
    userName: String,
    completedMissions: Int,
    totalMissions: Int,
    onPlay: () -> Unit,
    onOpenMissions: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "BERMAIN & BELAJAR",
                    fontSize = 11.sp,
                    letterSpacing = 1.2.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "Siap beraksi untuk bumi hari ini?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Pilih game seru dan selesaikan misi harianmu, $userName!",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onPlay() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SportsEsports,
                            contentDescription = null,
                            tint = GreenPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Main Game",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary
                        )
                    }
                }

                if (totalMissions > 0) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = YellowAccent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onOpenMissions() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.TaskAlt,
                                contentDescription = null,
                                tint = Color(0xFF6E5100)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$completedMissions/$totalMissions Misi",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6E5100)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsRow(gamesPlayed: Int, coinsEarned: Int, missionsDone: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MiniStatCard(
            label = "GAME DIMAIN",
            value = gamesPlayed.toString(),
            color = BluePrimary,
            icon = Icons.Filled.SportsEsports,
            modifier = Modifier.weight(1f)
        )
        MiniStatCard(
            label = "KOIN DIDAPAT",
            value = coinsEarned.toString(),
            color = Color(0xFFF9A825),
            icon = Icons.Filled.Star,
            modifier = Modifier.weight(1f)
        )
        MiniStatCard(
            label = "MISI SELESAI",
            value = missionsDone.toString(),
            color = GreenPrimary,
            icon = Icons.Filled.EmojiEvents,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MiniStatCard(
    label: String,
    value: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                fontSize = 9.sp,
                letterSpacing = 0.8.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DailyMissionsPreview(
    missions: List<DailyMission>,
    onOpenMissions: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(title = "Misi Harian", onOpen = onOpenMissions)

        if (missions.isEmpty()) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = "Belum ada misi hari ini. Yuk main game dulu!",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        } else {
            missions.take(3).forEach { mission ->
                MissionRow(mission = mission, onClick = onOpenMissions)
            }
        }
    }
}

@Composable
private fun MissionRow(mission: DailyMission, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (mission.isCompleted) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (mission.isCompleted) GreenPrimary else YellowAccent.copy(alpha = 0.5f)
            ) {
                Icon(
                    imageVector = if (mission.isCompleted) Icons.Filled.TaskAlt else Icons.Filled.Star,
                    contentDescription = null,
                    tint = if (mission.isCompleted) Color.White else Color(0xFF6E5100),
                    modifier = Modifier.padding(8.dp).size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = mission.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (mission.voucherName != null) {
                        Text(
                            text = "Hadiah: ${mission.voucherName}",
                            fontSize = 10.sp,
                            color = GreenPrimary
                        )
                    }
                }
                LinearProgressIndicator(
                    progress = { mission.progressPercent.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = if (mission.isCompleted) GreenPrimary else BluePrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
                Text(
                    text = if (mission.isCompleted) "Selesai!" else "${mission.progressValue}/${mission.targetValue} ${mission.description}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun GamesSection(onPlay: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(title = "Pilih Game")
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 3.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPlay() }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(BluePrimary, GreenPrimary))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SportsEsports,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Buka Menu Game",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "7 game edukasi siap dimainkan",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, onOpen: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (onOpen != null) {
            Text(
                text = "LIHAT SEMUA",
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onOpen() }
            )
        }
    }
}

// ── Game selection modal ──────────────────────────────────────────────────

private data class GameEntry(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

private val gameEntries = listOf(
    GameEntry("Kuis SDG", "Jawab soal tentang tujuan pembangunan berkelanjutan", Icons.Filled.Quiz, GreenPrimary, Screen.Quiz.route),
    GameEntry("Pilah Sampah", "Tarik-taruh sampah ke tong yang tepat", Icons.Filled.Delete, Color(0xFF2E7D32), Screen.TrashSort.route),
    GameEntry("Pasang Kartu SDG", "Cocokkan pernyataan dengan SDG-nya", Icons.Filled.Style, BluePrimary, Screen.MatchCard.route),
    GameEntry("Myth & Fact", "Tebak benar atau mitos", Icons.Filled.FactCheck, Color(0xFF8E24AA), Screen.MythFact.route),
    GameEntry("Clean River", "Bersihkan sungai dari sampah", Icons.Filled.Waves, Color(0xFF0277BD), Screen.CleanRiver.route),
    GameEntry("Arithmetic Kilat", "Hitung cepat dalam hitungan detik", Icons.Filled.Calculate, Color(0xFFEF6C00), Screen.ArithmeticKilat.route),
    GameEntry("Sudoku", "Isi angka tanpa berulang", Icons.Filled.GridOn, Color(0xFF00695C), Screen.Sudoku.route)
)

@Composable
private fun ChooseGameDialog(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Pilih Game",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Semua game gratis dan cocok untuk semua umur.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.height(390.dp)
                ) {
                    items(gameEntries) { game ->
                        GameTile(game = game, onClick = { onSelect(game.route) })
                    }
                }
            }
        }
    }
}

@Composable
private fun GameTile(game: GameEntry, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = game.color.copy(alpha = 0.12f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(game.color),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = game.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Text(
                text = game.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = game.subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                maxLines = 2
            )
        }
    }
}

// ── Missions modal ─────────────────────────────────────────────────────────

@Composable
private fun MissionsDialog(
    missions: List<DailyMission>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Misi Harian",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${missions.count { it.isCompleted }}/${missions.size} selesai",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.School,
                            contentDescription = "Tutup",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                if (missions.isEmpty()) {
                    Text(
                        text = "Belum ada misi aktif hari ini.",
                        modifier = Modifier.padding(vertical = 20.dp),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(missions) { mission ->
                            MissionRow(mission = mission) {}
                        }
                    }
                }
            }
        }
    }
}

// ── Settings modal ─────────────────────────────────────────────────────────

@Composable
private fun SettingsDialog(
    name: String,
    email: String,
    onDismiss: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenVouchers: () -> Unit,
    onOpenApiSettings: () -> Unit,
    onLogout: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFFB7EFC5), GreenPrimary))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("").uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = email,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                SettingsOption("Profil Saya", Icons.Filled.School, onOpenProfile)
                SettingsOption("Pencapaian Game", Icons.Filled.EmojiEvents, onOpenAchievements)
                SettingsOption("Voucher Saya", Icons.Filled.Star, onOpenVouchers)
                SettingsOption("Pengaturan Koneksi API", Icons.Filled.Settings, onOpenApiSettings)

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFEBEE),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLogout() }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Keluar",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsOption(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF4FBF6))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}