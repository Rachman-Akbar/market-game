package com.example.marketgame.data.remote

import com.example.marketgame.R
import com.example.marketgame.data.model.MythFactStatement
import com.example.marketgame.data.model.QuizQuestion
import com.example.marketgame.data.model.SdgGoal
import com.example.marketgame.data.model.SdgStatement
import com.example.marketgame.data.model.TrashItem

/**
 * Singleton repository for fetching game-relevant data from the backend:
 * content (soal/items/kartu), products, missions/quests, cart, and orders.
 */
object GameDataRepository {

    private val api: ApiService
        get() = ApiClient.getInstance(
            ApiClient.getAppContext()
                ?: throw IllegalStateException("ApiClient not initialized. Call ApiClient.getInstance(context) first.")
        )

    // ── Game content (real data from the database) ───────────────────────

    suspend fun getQuizQuestions(): Result<List<QuizQuestion>> {
        return try {
            val rows = api.getGameContent("quiz").data ?: emptyList()
            Result.success(rows.mapNotNull { it.toQuizQuestion() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMythFactStatements(): Result<List<MythFactStatement>> {
        return try {
            val rows = api.getGameContent("myth_fact").data ?: emptyList()
            Result.success(rows.mapNotNull { it.toMythFactStatement() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrashItems(): Result<List<TrashItem>> {
        return try {
            val rows = api.getGameContent("trash_sort").data ?: emptyList()
            Result.success(rows.mapNotNull { it.toTrashItem() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMatchCardDeck(): Result<Pair<List<SdgGoal>, List<SdgStatement>>> {
        return try {
            val rows = api.getGameContent("match_card").data ?: emptyList()
            val deck = rows.firstOrNull()?.payload
            if (deck == null) {
                Result.failure(RuntimeException("Dek Match Card belum tersedia."))
            } else {
                Result.success(deck.toMatchCardDeck())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGameSummary(): Result<GameSummaryResponse> {
        return try {
            val data = api.getGameSummary().data
            if (data != null) {
                Result.success(data)
            } else {
                Result.failure(RuntimeException("Ringkasan permainan tidak tersedia."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
        questions: List<com.example.marketgame.data.model.ArithmeticQuestion>,
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

    // ── Vouchers (User-owned) ────────────────────────────────────────────

    suspend fun getMyVouchers(): Result<List<MyVoucherResponse>> {
        return try {
            Result.success(api.getMyVouchers().data ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun claimVoucher(voucherId: Int): Result<MyVoucherResponse> {
        return try {
            val data = api.claimVoucher(voucherId).data
            if (data != null) {
                Result.success(data)
            } else {
                Result.failure(RuntimeException("Gagal mengklaim voucher."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Parsing konten dari DB ────────────────────────────────────────────

    private fun GameContentRow.toQuizQuestion(): QuizQuestion? {
        val p = payload ?: return null
        val options = p.stringList("options")
        if (options.isEmpty()) return null
        return QuizQuestion(
            question = p.string("question") ?: title ?: return null,
            options = options,
            correctAnswer = p.string("correct_answer") ?: return null,
            difficulty = p.string("difficulty") ?: difficulty ?: "",
            explanation = p.string("explanation") ?: ""
        )
    }

    private fun GameContentRow.toMythFactStatement(): MythFactStatement? {
        val p = payload ?: return null
        return MythFactStatement(
            id = id,
            statement = p.string("statement") ?: title ?: return null,
            isFact = p.getAsBoolean("is_fact") ?: false,
            imageUrl = p.string("image_url")
                ?: "https://lh3.googleusercontent.com/aida-public/AB6AXuCz_4TXD7jRsuq4r5m1U4h2yq7s_GG_xrTQ0Z7k8kL-0V1c8B9it8e9rjWfVh0tL4xU1mQAEf1B7D0gK_5oKrgyHulWzTqWvVx9NfJm1k8Y9"
        )
    }

    private fun GameContentRow.toTrashItem(): TrashItem? {
        val p = payload ?: return null
        val binColor = p.string("bin_color") ?: return null
        return TrashItem(
            name = p.string("name") ?: title ?: return null,
            correctBinColor = binColor,
            category = p.string("category") ?: "",
            imageRes = trashImageRes(p.string("image_key"))
        )
    }

    private fun trashImageRes(key: String?): Int = when (key) {
        "botolplastik" -> R.drawable.botolplastik
        "kresek" -> R.drawable.kresek
        "gelasplastik" -> R.drawable.gelasplastik
        "baterai" -> R.drawable.baterai
        "lampurusak" -> R.drawable.lampurusak
        "kalengcat" -> R.drawable.kalengcat
        "kulitpisang" -> R.drawable.kulitpisang
        "sayur" -> R.drawable.sayur
        "daunkering" -> R.drawable.daunkering
        "koran" -> R.drawable.koran
        "kardus" -> R.drawable.kardus
        "buku" -> R.drawable.buku
        "popok" -> R.drawable.popok
        "puntungrokok" -> R.drawable.puntungrokok
        "masker" -> R.drawable.masker
        else -> R.drawable.botolplastik
    }

    private fun Map<String, Any?>.toMatchCardDeck(): Pair<List<SdgGoal>, List<SdgStatement>> {
        val goals = (get("goals") as? List<*>)?.mapNotNull { element ->
            val obj = element as? Map<*, *> ?: return@mapNotNull null
            val number = (obj["goal_number"] as? Number)?.toInt() ?: return@mapNotNull null
            val name = (obj["name"] as? String) ?: "SDG $number"
            SdgGoal(id = number, title = name)
        } ?: emptyList()

        val statements = (get("statements") as? List<*>)?.mapNotNull { element ->
            val obj = element as? Map<*, *> ?: return@mapNotNull null
            val id = (obj["id"] as? Number)?.toInt() ?: return@mapNotNull null
            val goalId = (obj["goal_id"] as? Number)?.toInt() ?: return@mapNotNull null
            val text = (obj["text"] as? String) ?: return@mapNotNull null
            SdgStatement(id = id, text = text, goalId = goalId)
        } ?: emptyList()

        return goals to statements
    }

    private fun Map<String, Any?>.string(key: String): String? =
        get(key) as? String

    private fun Map<String, Any?>.stringList(key: String): List<String> =
        (get(key) as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

    private fun Map<String, Any?>.getAsBoolean(key: String): Boolean? =
        get(key) as? Boolean
}
