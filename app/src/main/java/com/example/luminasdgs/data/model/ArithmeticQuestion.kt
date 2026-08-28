package com.example.luminasdgs.data.model

data class ArithmeticQuestion(
    val operandA: Int,
    val operator: String,
    val operandB: Int,
    val difficulty: String
) {
    val answer: Int
        get() = when (operator) {
            "+" -> operandA + operandB
            "-" -> operandA - operandB
            "*" -> operandA * operandB
            "/" -> if (operandB == 0) 0 else operandA / operandB
            else -> 0
        }

    val display: String
        get() = "$operandA $operator $operandB"
}
