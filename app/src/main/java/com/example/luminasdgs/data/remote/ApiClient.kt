package com.example.luminasdgs.data.remote

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

    @Volatile
    private var retrofit: Retrofit? = null

    @Volatile
    private var apiService: ApiService? = null

    private var appContext: Context? = null

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    // ── Public API ───────────────────────────────────────────────────────

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
            retrofit = null
            apiService = null
            buildRetrofit(context.applicationContext)
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
            val dynamicUrl = baseUrlManager.getBaseUrl()

            val newUrl = original.url.newBuilder()
                .scheme(okhttp3.HttpUrl.parse(dynamicUrl)?.scheme() ?: "http")
                .host(okhttp3.HttpUrl.parse(dynamicUrl)?.host() ?: original.url.host())
                .port(okhttp3.HttpUrl.parse(dynamicUrl)?.port() ?: original.url.port())
                .build()

            val newRequest = original.newBuilder()
                .url(newUrl)
                .build()

            return chain.proceed(newRequest)
        }
    }

    /**
     * Attaches the Bearer token from SharedPreferences if available.
     * Token key must match what you save after login.
     */
    private fun authInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val prefs = context.getSharedPreferences("api_config_prefs", Context.MODE_PRIVATE)
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
     * Logs full request/response in DEBUG builds. Remove or silence in production.
     */
    private fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}
