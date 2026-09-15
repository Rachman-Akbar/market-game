package com.example.marketgame.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.marketgame.navigation.Screen
import com.example.marketgame.ui.theme.GreenPrimary
import com.example.marketgame.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController? = null,
    viewModel: ProfileViewModel = viewModel()
) {
    val profile by viewModel.profile.collectAsState()

    val stats = listOf(
        StatItem("JAWABAN BENAR", profile.xp.toString(), Icons.Filled.Star, Color(0xFF4CAF50)),
        StatItem("KOIN DIDAPAT", profile.coins.toString(), Icons.Filled.EmojiEvents, Color(0xFFF9A825)),
        StatItem("GAME DIMAIN", profile.gamesPlayed.toString(), Icons.Filled.SportsEsports, Color(0xFF0288D1)),
        StatItem("MISI SELESAI", profile.completedQuest.toString(), Icons.Filled.Star, Color(0xFF2E7D32))
    )

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
    ) {
        item {
            ProfileTopBar(onBack = { navController?.popBackStack() })
        }

        item {
            ProfileHeroCard(profile = profile)
        }

        item {
            StatsGrid(stats = stats)
        }

        item {
            AchievementsSection(onOpen = { navController?.navigate(Screen.AchievementHub.route) })
        }

        item {
            VouchersSection(onOpen = { navController?.navigate(Screen.Vouchers.route) })
        }

        item {
            AccountSettingsSection(
                onOpenApiSettings = { navController?.navigate(Screen.ApiSettings.route) },
                onLogout = {
                    viewModel.logout {
                        navController?.navigate(Screen.Login.route) {
                            popUpTo(Screen.Profile.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Profil Saya",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ProfileHeroCard(profile: com.example.marketgame.data.model.UserProfile) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF7FBF5), Color(0xFFE8F5E9))
                    )
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFB7EFC5), GreenPrimary))
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (profile.avatar.isNullOrBlank()) {
                    Text(
                        text = profile.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("").uppercase(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                } else {
                    AsyncImage(
                        model = profile.avatar,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(96.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = profile.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (profile.email.isBlank()) "Petualang SDGs" else profile.email,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color(0xFFFFE082)
                ) {
                    Text(
                        text = "LEVEL ${profile.level}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6E5100)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsGrid(stats: List<StatItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(item = stats[0], modifier = Modifier.weight(1f))
            StatCard(item = stats[1], modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(item = stats[2], modifier = Modifier.weight(1f))
            StatCard(item = stats[3], modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(item: StatItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = item.icon, contentDescription = null, tint = item.color)
            }
            Text(
                text = item.label,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = item.value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = item.color
            )
        }
    }
}

@Composable
private fun AccountSettingsSection(
    onOpenApiSettings: () -> Unit,
    onLogout: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Pengaturan",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            SettingsRow("Pengaturan Koneksi API", Icons.Filled.Settings, onClick = onOpenApiSettings)
            SettingsRow("Keluar", Icons.Filled.Logout, isDestructive = true, onClick = onLogout)
        }
    }
}

@Composable
private fun AchievementsSection(onOpen: () -> Unit) {
    NavigationCard(
        title = "Game Achievement",
        subtitle = "Lihat materi kuis, kartu SDG, dan lainnya",
        icon = Icons.Filled.EmojiEvents,
        color = Color(0xFFEF6C00),
        onClick = onOpen
    )
}

@Composable
private fun VouchersSection(onOpen: () -> Unit) {
    NavigationCard(
        title = "Voucher Saya",
        subtitle = "Klaim voucher hasil misi",
        icon = Icons.Filled.CardGiftcard,
        color = Color(0xFF2E7D32),
        onClick = onOpen
    )
}

@Composable
private fun NavigationCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF9E9E9E)
            )
        }
    }
}

@Composable
private fun SettingsRow(
    label: String,
    icon: ImageVector,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    val tint = if (isDestructive) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary
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
            Icon(imageVector = icon, contentDescription = null, tint = tint)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = label, fontSize = 14.sp, color = tint)
        }
        if (!isDestructive) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF9E9E9E)
            )
        }
    }
}

private data class StatItem(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)