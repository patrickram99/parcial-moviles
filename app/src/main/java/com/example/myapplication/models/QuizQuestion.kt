package com.example.myapplication.models

/**
 * Data class representing a quiz question.
 * @param question The text of the quiz question.
 * @param options A list of possible answers to the quiz question.
 * @param correctAnswer The index of the correct answer in the options list.
 * @param explanation An explanation of the correct answer.
 */
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String
)