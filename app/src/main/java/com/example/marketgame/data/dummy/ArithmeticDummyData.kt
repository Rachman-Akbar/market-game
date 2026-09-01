package com.example.marketgame.data.dummy

import com.example.marketgame.data.model.ArithmeticQuestion
import kotlin.random.Random

object ArithmeticDummyData {

    fun generateQuestions(difficulty: String, count: Int = 15): List<ArithmeticQuestion> {
        return (1..count).map { generate(difficulty) }
    }

    private fun generate(difficulty: String): ArithmeticQuestion {
        val rnd = Random.Default
        return when (difficulty) {
            "Mudah" -> {
                val op = listOf("+", "-").random(rnd)
                when (op) {
                    "+" -> {
                        val a = rnd.nextInt(1, 11)
                        val b = rnd.nextInt(1, 11)
                        ArithmeticQuestion(a, "+", b, "Mudah")
                    }
                    else -> {
                        val a = rnd.nextInt(2, 11)
                        val b = rnd.nextInt(1, a + 1)
                        ArithmeticQuestion(a, "-", b, "Mudah")
                    }
                }
            }
            "Sedang" -> {
                val op = listOf("+", "-", "*").random(rnd)
                when (op) {
                    "+" -> {
                        val a = rnd.nextInt(10, 51)
                        val b = rnd.nextInt(1, 51)
                        ArithmeticQuestion(a, "+", b, "Sedang")
                    }
                    "-" -> {
                        val a = rnd.nextInt(20, 101)
                        val b = rnd.nextInt(1, a + 1)
                        ArithmeticQuestion(a, "-", b, "Sedang")
                    }
                    else -> {
                        val a = rnd.nextInt(2, 10)
                        val b = rnd.nextInt(2, 10)
                        ArithmeticQuestion(a, "*", b, "Sedang")
                    }
                }
            }
            else -> {
                val op = listOf("+", "-", "*", "/").random(rnd)
                when (op) {
                    "+" -> {
                        val a = rnd.nextInt(100, 501)
                        val b = rnd.nextInt(1, 501)
                        ArithmeticQuestion(a, "+", b, "Sulit")
                    }
                    "-" -> {
                        val a = rnd.nextInt(200, 1001)
                        val b = rnd.nextInt(1, a + 1)
                        ArithmeticQuestion(a, "-", b, "Sulit")
                    }
                    "/" -> {
                        val b = rnd.nextInt(2, 13)
                        val q = rnd.nextInt(2, 13)
                        ArithmeticQuestion(b * q, "/", b, "Sulit")
                    }
                    else -> {
                        val a = rnd.nextInt(10, 21)
                        val b = rnd.nextInt(10, 21)
                        ArithmeticQuestion(a, "*", b, "Sulit")
                    }
                }
            }
        }
    }
}
