package com.example.marketgame.ui.screens.profile.vouchers

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.marketgame.data.remote.MyVoucherResponse
import com.example.marketgame.data.remote.VoucherResponse
import com.example.marketgame.viewmodel.VouchersViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun VouchersScreen(
    navController: NavController,
    viewModel: VouchersViewModel = viewModel()
) {
    val vouchers by viewModel.vouchers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val claimId by viewModel.claimId.collectAsState()
    val message by viewModel.message.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            VouchersTopBar(onBack = { navController.popBackStack() })
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Voucher Saya",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Klaim voucher milik Anda di sini. Voucher yang sudah diklaim otomatis tampil di website.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        if (message != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = message.orEmpty(),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }

        if (errorMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = errorMessage.orEmpty(),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFC62828)
                    )
                }
            }
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        } else if (!errorMessage.isNullOrEmpty() && vouchers.isEmpty()) {
            item {
                VoucherEmptyState(onRetry = viewModel::loadVouchers)
            }
        } else if (vouchers.isEmpty()) {
            item {
                VoucherEmptyState(onRetry = viewModel::loadVouchers)
            }
        } else {
            items(vouchers, key = { it.id }) { item ->
                VoucherCard(
                    item = item,
                    submitting = isSubmitting && claimId == item.id,
                    onClaim = { viewModel.claim(item.id) }
                )
            }
        }
    }
}

@Composable
private fun VouchersTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB2DFDB)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "HQ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = "Hero Quest",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Surface(
            shape = RoundedCornerShape(999.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onBack() }
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "Kembali",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun VoucherCard(
    item: MyVoucherResponse,
    submitting: Boolean,
    onClaim: () -> Unit
) {
    val voucher = item.voucher
    val status = item.status ?: "available"
    val statusMeta = when (status) {
        "claimed" -> StatusMeta("Diklaim", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "used" -> StatusMeta("Terpakai", Color(0xFFF5F5F5), Color(0xFF9E9E9E))
        else -> StatusMeta("Belum diklaim", Color(0xFFFFF3E0), Color(0xFFEF6C00))
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CardGiftcard,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.padding(6.dp).size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = voucher?.name ?: "Voucher",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Kode: ${voucher?.code ?: "-"}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = statusMeta.background
                ) {
                    Text(
                        text = statusMeta.label,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusMeta.content
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    val discountText = discountText(voucher)
                    Text(
                        text = discountText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "Min. belanja ${formatIdr(voucher?.minSpend ?: 0)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = when (status) {
                        "used" -> "Terpakai ${formatDate(item.usedAt)}"
                        else -> "Berlaku sampai ${formatDate(voucher?.endsAt)}"
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Text(
                text = "Dari ${sourceLabel(item.sourceType)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            if (status == "available") {
                Button(
                    onClick = onClaim,
                    enabled = !submitting,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    if (submitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Mengklaim...")
                    } else {
                        Text(text = "Klaim Sekarang")
                    }
                }
            }
        }
    }
}

@Composable
private fun VoucherEmptyState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.LocalOffer,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = "Belum ada voucher",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Selesaikan misi lalu klaim voucher lewat aplikasi ini.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(999.dp)
        ) {
            Text(text = "Muat Ulang")
        }
    }
}

private data class StatusMeta(
    val label: String,
    val background: Color,
    val content: Color
)

private fun sourceLabel(sourceType: String?): String = when (sourceType) {
    "mission" -> "misi"
    "admin" -> "admin"
    else -> sourceType?.takeIf { it.isNotBlank() } ?: "voucher"
}

private fun discountText(voucher: VoucherResponse?): String {
    if (voucher == null) return "-"
    val value = voucher.discountValue ?: 0
    return when {
        voucher.discountTarget == "shipping" && voucher.discountType == "percentage" && value >= 100 -> "Gratis Ongkir"
        voucher.discountType == "percentage" -> "$value%"
        else -> formatIdr(value)
    }
}

private fun formatIdr(value: Int): String =
    "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(value)}"

private fun formatDate(value: String?): String {
    if (value.isNullOrBlank()) return "-"
    val normalized = value.replace("T", " ").removeSuffix("Z")
    return try {
        val parsed = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(normalized) ?: return normalized
        SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(parsed)
    } catch (_: Exception) {
        normalized
    }
}