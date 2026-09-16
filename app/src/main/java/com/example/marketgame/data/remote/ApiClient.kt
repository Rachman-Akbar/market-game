package com.example.marketgame.data.remote

import android.content.Context
import com.example.marketgame.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton Retrofit client with dynamic base URL support.
 *
 * Architecture:
 *   BaseUrlManager  →  DynamicBaseUrlInterceptor  →  OkHttpClient  →  Retrofit  →  ApiService
 *
 * The DynamicBaseUrlInterceptor reads the current URL from BaseUrlManager
 * on EVERY request, so changing the URL at runtime takes effect immediately
 * without rebuilding Retrofit.
 */
object ApiClient {

    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L

    class ApiException(message: String) : RuntimeException(message)

    private data class ErrorBody(val message: String?)

    @Volatile
    private var retrofit: Retrofit? = null

    @Volatile
    private var apiService: ApiService? = null

    private var appContext: Context? = null

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    // ── Public API ───────────────────────────────────────────────────────

    /**
     * Initializes the app context. Call from an Application subclass at startup
     * so repositories can be used anywhere without a Context.
     */
    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    fun getInstance(context: Context): ApiService {
        appContext = context.applicationContext
        return apiService ?: synchronized(this) {
            apiService ?: buildRetrofit(context).also { apiService = it }
        }
    }

    fun getAppContext(): Context? = appContext

    /**
     * Force-rebuild Retrofit after the base URL changes.
     * Call this after BaseUrlManager.setBaseUrl() or setEnvironment().
     */
    fun rebuild(context: Context) {
        synchronized(this) {
            appContext = context.applicationContext
            retrofit = null
            apiService = null
            apiService = buildRetrofit(context.applicationContext)
        }
    }

    // ── Internal ──────────────────────────────────────────────────────────

    private fun buildRetrofit(context: Context): ApiService {
        val baseUrlManager = BaseUrlManager.getInstance(context)

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(DynamicBaseUrlInterceptor(baseUrlManager))
            .addInterceptor(authInterceptor(context))
            .addInterceptor(errorMessageInterceptor())
            .addInterceptor(loggingInterceptor())
            .build()

        val instance = Retrofit.Builder()
            .baseUrl(baseUrlManager.getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit = instance
        return instance.create(ApiService::class.java)
    }

    // ── Interceptors ──────────────────────────────────────────────────────

    /**
     * Overrides the request URL with the dynamic base URL from BaseUrlManager.
     * This runs on every request, so runtime URL changes are picked up immediately.
     */
    private class DynamicBaseUrlInterceptor(
        private val baseUrlManager: BaseUrlManager
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val original = chain.request()
            val dynamic = baseUrlManager.getBaseUrl().toHttpUrlOrNull()
                ?: return chain.proceed(original)

            val newUrl = original.url.newBuilder()
                .scheme(dynamic.scheme)
                .host(dynamic.host)
                .port(dynamic.port)
                .build()

            val newRequest = original.newBuilder()
                .url(newUrl)
                .build()

            return chain.proceed(newRequest)
        }
    }

    /**
     * Attaches the Bearer token from secure storage if available.
     * Token key must match what you save after login.
     */
    private fun authInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val prefs = SecurePrefs.open(context)
            val token = prefs.getString("auth_token", null)

            val request = if (!token.isNullOrEmpty()) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept", "application/json")
                    .build()
            } else {
                chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .build()
            }

            chain.proceed(request)
        }
    }

    /**
     * Logs request/response bodies ONLY in debug builds.
     * Production builds never log payloads (avoids leaking auth tokens & PII).
     */
    private fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * Mengubah respons error non-2xx menjadi [ApiException] dengan pesan dari
     * body backend ({message, errors}), sehingga layar/ViewModel bisa menampilkan
     * keterangan asli (mis. "Email belum diverifikasi") alih-alih "HTTP 403".
     */
    private fun errorMessageInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            if (response.isSuccessful) {
                return@Interceptor response
            }

            val rawBody = response.peekBody(Long.MAX_VALUE).string()
            val message = runCatching {
                moshi.adapter(ErrorBody::class.java).fromJson(rawBody)?.message
            }.getOrNull().orEmpty().trim().ifBlank { null } ?: rawBody.ifBlank { null }

            response.close()

            if (message != null) {
                throw ApiException(message)
            }

            response
        }
    }
}
