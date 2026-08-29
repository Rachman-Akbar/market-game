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
            val data = api.getProduct(id).data
            if (data != null) {
                Result.success(data)
            } else {
                Result.failure(RuntimeException("Produk tidak ditemukan."))
            }
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
            val data = api.reportGameCompletion(GameCompletionRequest(eventType, value)).data
            if (data != null) {
                Result.success(data)
            } else {
                Result.failure(RuntimeException("Gagal melaporkan misi."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reportArithmeticKilat(
        sessionId: String,
        durationSeconds: Int,
        difficulty: String,
        questions: List<com.example.luminasdgs.data.model.ArithmeticQuestion>,
        userAnswers: List<Int?>
    ): Result<GameReportResponse> {
        return try {
            val payload = questions.mapIndexed { index, q ->
                GameQuestionPayload(
                    operand_a = q.operandA,
                    operator = q.operator,
                    operand_b = q.operandB,
                    user_answer = userAnswers.getOrNull(index)
                )
            }
            val request = GameReportRequest(
                game_type = "arithmetic_kilat",
                session_id = sessionId,
                duration_seconds = durationSeconds,
                difficulty = difficulty,
                questions = payload,
                grid = null
            )
            Result.success(api.reportGame(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reportSudoku(
        sessionId: String,
        durationSeconds: Int,
        difficulty: String,
        grid: List<Int>
    ): Result<GameReportResponse> {
        return try {
            val request = GameReportRequest(
                game_type = "sudoku",
                session_id = sessionId,
                duration_seconds = durationSeconds,
                difficulty = difficulty,
                questions = null,
                grid = grid
            )
            Result.success(api.reportGame(request))
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
            val result = api.addToCart(mapOf("product_id" to productId, "quantity" to quantity))
            val data = result.data
            if (data != null) {
                Result.success(data)
            } else {
                Result.failure(RuntimeException(result.message ?: "Gagal menambahkan ke keranjang."))
            }
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
