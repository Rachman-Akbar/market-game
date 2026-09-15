plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// Catatan: google-services.json (app/) dipakai sebagai referensi nilai project.
// Firebase di-init secara programatik di FirebaseGoogleAuth (FirebaseOptions)
// memakai value Android di bawah — tanpa plugin google-services, supaya tidak
// menarik firebase-bom 34.x yang butuh Kotlin 2.3+ (proyek ini Kotlin 2.0.21).

// Google OAuth 2.0 "Web client ID" dari Firebase project "marketplace-village".
// Nilai default diambil dari app/google-services.json (oauth_client client_type 3).
// Bisa dioverride tanpa mengedit file ini:
//   1) tulis di ~/.gradle/gradle.properties atau local.properties:
//        GOOGLE_SIGNIN_CLIENT_ID=698326217840-2pd9kqf44rol0se7kov9l1b5n99pcmk0.apps.googleusercontent.com
//   2) atau export GOOGLE_SIGNIN_CLIENT_ID=... (environment)
val googleSignInClientId: String =
    project.findProperty("GOOGLE_SIGNIN_CLIENT_ID") as? String
        ?: System.getenv("GOOGLE_SIGNIN_CLIENT_ID")
        ?: "698326217840-2pd9kqf44rol0se7kov9l1b5n99pcmk0.apps.googleusercontent.com"

// Real-time Database URL — disamakan dengan backend (market-api .env).
val firebaseDatabaseUrl: String =
    project.findProperty("FIREBASE_DATABASE_URL") as? String
        ?: System.getenv("FIREBASE_DATABASE_URL")
        ?: "https://marketplace-village-default-rtdb.asia-southeast1.firebasedatabase.app"

android {
    namespace = "com.example.marketgame"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.marketgame"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // Firebase project marketplace-village — nilai dari app/google-services.json
        // (pakai value khusus Android, bukan value Web, supaya API key/restriction cocok).
        buildConfigField("String", "FIREBASE_API_KEY", "\"AIzaSyCnwXPRFye50PyHmnxq5eXc5iE8GJ7HDFU\"")
        buildConfigField("String", "FIREBASE_PROJECT_ID", "\"marketplace-village\"")
        buildConfigField("String", "FIREBASE_APP_ID", "\"1:698326217840:android:3d5cb0152da7d635f8b0b5\"")
        buildConfigField("String", "FIREBASE_STORAGE_BUCKET", "\"marketplace-village.firebasestorage.app\"")
        buildConfigField("String", "FIREBASE_AUTH_DOMAIN", "\"marketplace-village.firebaseapp.com\"")
        buildConfigField("String", "FIREBASE_DATABASE_URL", "\"$firebaseDatabaseUrl\"")
        // OAuth web client ID dari google-services.json (client_type 3). Dipakai
        // requestIdToken(...) pada GoogleSignInOptions.
        buildConfigField("String", "GOOGLE_SIGNIN_CLIENT_ID", "\"$googleSignInClientId\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)

    // Networking - Retrofit + OkHttp + Moshi
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)

    // Firebase Auth + Google Sign-In (backend verifies via service account)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.auth)
    implementation(libs.kotlinx.coroutines.play.services)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

}