package com.example.marketgame.ui.screens.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketgame.data.remote.FirebaseGoogleAuth

/**
 * Tombol "Lanjut dengan Google". Memakai GoogleSignInClient + FirebaseAuth,
 * lalu mengirimkan akun Google ke callback untuk ditukar menjadi ID token.
 */
@Composable
fun GoogleSignInButton(
    enabled: Boolean = true,
    onAccountReceived: (com.google.android.gms.auth.api.signin.GoogleSignInAccount) -> Unit,
    onNotConfigured: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val account = FirebaseGoogleAuth.getAccountFromIntent(result.data)
            if (account != null) {
                onAccountReceived(account)
            }
        }
    }

    OutlinedButton(
        onClick = {
            if (!FirebaseGoogleAuth.isConfigured()) {
                onNotConfigured()
                return@OutlinedButton
            }
            val intent = FirebaseGoogleAuth.signInClient(context).signInIntent
            launcher.launch(intent)
        },
        enabled = enabled,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3C4043)),
        modifier = Modifier
            .height(52.dp)
            .then(Modifier.fillMaxWidth())
    ) {
        GoogleLogo()
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Lanjut dengan Google",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun GoogleLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "G",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4285F4)
        )
    }
}