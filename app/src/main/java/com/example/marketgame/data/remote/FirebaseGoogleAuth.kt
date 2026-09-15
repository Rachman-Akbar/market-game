package com.example.marketgame.data.remote

import android.content.Context
import android.util.Log
import com.example.marketgame.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

/**
 * Membantu login Google memakai Firebase, sama seperti market-frontend.
 *
 * Alur:
 *  1. [signInClient] menghasilkan intent Google Sign-In (requestIdToken dengan
 *     web client ID project marketplace-village).
 *  2. Setelah akun kembali di layar, [getFirebaseIdToken] menukar kredensial
 *     Google menjadi ID token Firebase melalui GoogleAuthProvider.
 *  3. ID token tersebut dikirim backend ke `identity/auth/firebase-login`
 *     (server memverifikasi dengan service account, lalu membuat/melink user).
 */
object FirebaseGoogleAuth {

    private val TAG = "FirebaseGoogleAuth"

    private fun ensureInitialized(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            val options = FirebaseOptions.Builder()
                .setApplicationId(BuildConfig.FIREBASE_APP_ID)
                .setApiKey(BuildConfig.FIREBASE_API_KEY)
                .setProjectId(BuildConfig.FIREBASE_PROJECT_ID)
                .setStorageBucket(BuildConfig.FIREBASE_STORAGE_BUCKET)
                .setDatabaseUrl(BuildConfig.FIREBASE_DATABASE_URL)
                .build()
            FirebaseApp.initializeApp(context, options)
        }
    }

    fun isConfigured(): Boolean = BuildConfig.GOOGLE_SIGNIN_CLIENT_ID.isNotBlank()

    fun signInClient(context: Context): GoogleSignInClient {
        ensureInitialized(context)
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SIGNIN_CLIENT_ID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, options)
    }

    fun getAccountFromIntent(data: android.content.Intent?): GoogleSignInAccount? {
        if (data == null) return null
        return runCatching {
            GoogleSignIn.getSignedInAccountFromIntent(data).getResult(com.google.android.gms.common.api.ApiException::class.java)
        }.getOrElse { e ->
            Log.w(TAG, "Gagal membaca akun Google dari intent", e)
            null
        }
    }

    suspend fun getFirebaseIdToken(context: Context, account: GoogleSignInAccount): String {
        ensureInitialized(context)
        val googleIdToken = account.idToken ?: throw IllegalStateException(
            "Google Sign-In tidak mengembalikan ID token. Pastikan GOOGLE_SIGNIN_CLIENT_ID sudah diisi."
        )
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        val auth = FirebaseAuth.getInstance()
        val authResult = auth.signInWithCredential(credential).await()
        val firebaseUser = authResult.user ?: auth.currentUser
            ?: throw IllegalStateException("Firebase tidak mengembalikan user.")
        return firebaseUser.getIdToken(true).await().token
            ?: throw IllegalStateException("Gagal memperoleh Firebase ID token.")
    }

    suspend fun signOut(context: Context) {
        runCatching { FirebaseAuth.getInstance().signOut() }
        runCatching { signInClient(context).signOut().await() }
    }
}