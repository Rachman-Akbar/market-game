package com.example.luminasdgs.data.remote

/**
 * Singleton repository for fetching game-relevant data from the backend:
 * products, missions/quests, cart, and orders.
 */
object GameDataRepository {

    private val api: ApiService
        get() = ApiClient.getInstance(
            ApiClient.getAppContext()
                ?: throw IllegalStateException("ApiClient not initialized. Call ApiClient.getInstance(context) first.")
        )

    // ── Products ─────────────────────────────────────────────────────────

    suspend fun getProducts(
        page: Int? = null,
        search: String? = null
    ): Result<PaginatedResponse<ProductResponse>> {
        return try {
            Result.success(api.getProducts(page = page, search = search))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProduct(id: Int): Result<ProductResponse> {
        return try {
            Result.success(api.getProduct(id).data!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Missions / Game Quests ───────────────────────────────────────────

    suspend fun getMissions(): Result<List<MissionResponse>> {
        return try {
            Result.success(api.getMissions().data ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyMissions(): Result<List<MissionResponse>> {
        return try {
            Result.success(api.getMyMissions().data ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reportGameCompletion(eventType: String, value: Int): Result<MissionReportResponse> {
        return try {
            Result.success(api.reportGameCompletion(GameCompletionRequest(eventType, value)).data!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Cart ─────────────────────────────────────────────────────────────

    suspend fun getCart(): Result<List<CartItemResponse>> {
        return try {
            Result.success(api.getCart().data ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToCart(productId: Int, quantity: Int = 1): Result<CartItemResponse> {
        return try {
            Result.success(api.addToCart(mapOf("product_id" to productId, "quantity" to quantity)).data!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Orders ───────────────────────────────────────────────────────────

    suspend fun getOrders(): Result<List<OrderResponse>> {
        return try {
            Result.success(api.getOrders().data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
