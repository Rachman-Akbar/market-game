package com.example.marketgame.data.remote

import okhttp3.ResponseBody
import retrofit2.http.*

// ── Auth ─────────────────────────────────────────────────────────────────

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val email: String, val password: String, val password_confirmation: String)
data class AuthResponse(val message: String, val user: UserResponse?, val token: String?)

// ── User ─────────────────────────────────────────────────────────────────

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String?,
    val avatar: String?,
    val created_at: String?
)

// ── Generic wrapper (matches Laravel API ResourceCollection shape) ────────

data class ApiResponse<T>(
    val message: String?,
    val data: T?
)

data class PaginatedResponse<T>(
    val data: List<T>,
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)

// ── Product (Catalog) ────────────────────────────────────────────────────

data class ProductResponse(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    val price: String,
    val image_url: String?,
    val category: CategoryResponse?,
    val stock: Int?
)

data class CategoryResponse(
    val id: Int,
    val name: String,
    val slug: String
)

// ── Mission (Engagement / Game Quests) ───────────────────────────────────

data class MissionResponse(
    val id: Int,
    val title: String,
    val description: String?,
    val points: Int,
    val type: String?,
    val status: String?
)

data class GameCompletionRequest(val event_type: String, val value: Int)
data class MissionReportResponse(val missions_updated: Int, val rewards_earned: List<String>)

data class GameQuestionPayload(
    val operand_a: Int,
    val operator: String,
    val operand_b: Int,
    val user_answer: Int?
)

data class GameReportRequest(
    val game_type: String,
    val session_id: String,
    val duration_seconds: Int,
    val difficulty: String?,
    val questions: List<GameQuestionPayload>?,
    val grid: List<Int>?
)

data class GameReportResponse(
    val session: GameSessionResponse?,
    val success: Boolean?,
    val message: String?
)

data class GameSessionResponse(
    val id: Int,
    val game_type: String?,
    val session_id: String?,
    val score: Int,
    val correct_count: Int?,
    val total_questions: Int?,
    val duration_seconds: Int?,
    val difficulty: String?,
    val coins_awarded: Int?
)

// ── Order ────────────────────────────────────────────────────────────────

data class CartItemResponse(
    val id: Int,
    val product: ProductResponse?,
    val quantity: Int,
    val subtotal: String
)

data class OrderResponse(
    val id: Int,
    val status: String,
    val total: String,
    val items: List<OrderItemResponse>?
)

data class OrderItemResponse(
    val id: Int,
    val product: ProductResponse?,
    val quantity: Int,
    val price: String
)

// ── Voucher (User-owned) ─────────────────────────────────────────────────

data class VoucherResponse(
    val id: Int,
    val code: String,
    val name: String,
    val image: String?,
    val imageUrl: String?,
    val voucherScope: String?,
    val discountTarget: String?,
    val discountType: String?,
    val discountValue: Int?,
    val minSpend: Int?,
    val minItems: Int?,
    val minDistinctProducts: Int?,
    val terms: String?,
    val maxDiscount: Int?,
    val startsAt: String?,
    val endsAt: String?,
    val usageLimit: Int?,
    val usedCount: Int?,
    val storeId: Int?,
    val storeName: String?,
    val isActive: Boolean?,
    val createdAt: String?
)

data class MyVoucherResponse(
    val id: Int,
    val status: String?,
    val claimedAt: String?,
    val usedAt: String?,
    val sourceType: String?,
    val sourceId: Int?,
    val voucher: VoucherResponse?
)

// ══════════════════════════════════════════════════════════════════════════
// Retrofit Service Interface
// ══════════════════════════════════════════════════════════════════════════

interface ApiService {

    // ── Auth ─────────────────────────────────────────────────────────────

    @POST("identity/auth/password-login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<AuthResponse>

    @POST("identity/auth/password-register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<AuthResponse>

    @GET("identity/auth/me")
    suspend fun getMe(): ApiResponse<UserResponse>

    @POST("identity/auth/logout")
    suspend fun logout(): ApiResponse<Unit>

    // ── Products (Public) ────────────────────────────────────────────────

    @GET("catalog/products")
    suspend fun getProducts(
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null
    ): PaginatedResponse<ProductResponse>

    @GET("catalog/products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ApiResponse<ProductResponse>

    @GET("catalog/products/slug/{slug}")
    suspend fun getProductBySlug(@Path("slug") slug: String): ApiResponse<ProductResponse>

    @GET("catalog/categories")
    suspend fun getCategories(): ApiResponse<List<CategoryResponse>>

    // ── Cart ─────────────────────────────────────────────────────────────

    @GET("order/carts")
    suspend fun getCart(): ApiResponse<List<CartItemResponse>>

    @POST("order/carts/items")
    suspend fun addToCart(@Body body: Map<String, Int>): ApiResponse<CartItemResponse>

    @PATCH("order/carts/items/{id}")
    suspend fun updateCartItem(
        @Path("id") id: Int,
        @Body body: Map<String, Int>
    ): ApiResponse<CartItemResponse>

    @DELETE("order/carts/items/{id}")
    suspend fun removeFromCart(@Path("id") id: Int): ApiResponse<Unit>

    // ── Orders ───────────────────────────────────────────────────────────

    @GET("order/orderings")
    suspend fun getOrders(@Query("page") page: Int? = null): PaginatedResponse<OrderResponse>

    @POST("order/orderings")
    suspend fun createOrder(@Body body: Map<String, Any?>): ApiResponse<OrderResponse>

    // ── Vouchers (User-owned) ────────────────────────────────────────────

    @GET("order/vouchers/mine")
    suspend fun getMyVouchers(): ApiResponse<List<MyVoucherResponse>>

    @POST("order/vouchers/{id}/claim")
    suspend fun claimVoucher(@Path("id") id: Int): ApiResponse<MyVoucherResponse>

    // ── Missions / Game Quests ───────────────────────────────────────────

    @GET("engagement/missions")
    suspend fun getMissions(): ApiResponse<List<MissionResponse>>

    @GET("engagement/missions/me")
    suspend fun getMyMissions(): ApiResponse<List<MissionResponse>>

    @POST("engagement/missions/report")
    suspend fun reportGameCompletion(@Body body: GameCompletionRequest): ApiResponse<MissionReportResponse>

    @POST("engagement/games/report")
    suspend fun reportGame(@Body body: GameReportRequest): GameReportResponse

    // ── Users ────────────────────────────────────────────────────────────

    @GET("identity/users/{id}")
    suspend fun getUser(@Path("id") id: Int): ApiResponse<UserResponse>

    @PUT("identity/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): ApiResponse<UserResponse>
}
