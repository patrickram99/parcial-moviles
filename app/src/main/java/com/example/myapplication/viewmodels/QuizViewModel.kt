package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.models.QuizQuestion
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * ViewModel class for managing quiz data and logic.
 * @param application The application context.
 */
class QuizViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData holding the list of quiz questions
    private val _questions = MutableLiveData<List<QuizQuestion>>()
    val questions: LiveData<List<QuizQuestion>> = _questions

    // LiveData holding the current question index
    private val _currentQuestionIndex = MutableLiveData<Int>()
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    // LiveData holding the current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    // LiveData holding the leaderboard entries
    private val _leaderboard = MutableLiveData<List<LeaderboardEntry>>()
    val leaderboard: LiveData<List<LeaderboardEntry>> = _leaderboard

    // Player's name
    var playerName: String = ""

    /**
     * Initializes the ViewModel by loading questions and leaderboard data.
     */
    init {
        loadQuestions()
        loadLeaderboard()
        _currentQuestionIndex.value = 0
        _score.value = 0
    }

    /**
     * Loads quiz questions from a JSON file in the assets folder.
     */
    private fun loadQuestions() {
        val jsonString = getApplication<Application>().assets.open("quiz_questions.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)
        val questionList = mutableListOf<QuizQuestion>()

        for (i in 0 until jsonArray.length()) {
            val questionObj = jsonArray.getJSONObject(i)
            val options = mutableListOf<String>()
            val optionsArray = questionObj.getJSONArray("options")
            for (j in 0 until optionsArray.length()) {
                options.add(optionsArray.getString(j))
            }
            questionList.add(
                QuizQuestion(
                    questionObj.getString("question"),
                    options,
                    questionObj.getInt("correctAnswer"),
                    questionObj.getString("explanation")
                )
            )
        }

        // Select 5 random questions
        val randomQuestions = questionList.shuffled().take(5)
        _questions.value = randomQuestions
    }

    /**
     * Loads leaderboard data from a JSON file in the assets folder.
     */
    private fun loadLeaderboard() {
        val jsonString = getApplication<Application>().assets.open("leaderboard.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)
        val leaderboardList = mutableListOf<LeaderboardEntry>()

        for (i in 0 until jsonArray.length()) {
            val entryObj = jsonArray.getJSONObject(i)
            leaderboardList.add(
                LeaderboardEntry(
                    entryObj.getString("name"),
                    entryObj.getInt("score")
                )
            )
        }

        _leaderboard.value = leaderboardList
    }

    /**
     * Saves the current leaderboard data to a JSON file in the app's files directory.
     */
    private fun saveLeaderboard() {
        val jsonArray = JSONArray()
        _leaderboard.value?.forEach { entry ->
            val entryObj = JSONObject()
            entryObj.put("name", entry.name)
            entryObj.put("score", entry.score)
            jsonArray.put(entryObj)
        }

        val file = File(getApplication<Application>().filesDir, "leaderboard.json")
        file.writeText(jsonArray.toString())
    }

    /**
     * Returns the current quiz question.
     * @return The current QuizQuestion or null if the index is out of bounds.
     */
    fun getCurrentQuestion(): QuizQuestion? {
        return questions.value?.getOrNull(currentQuestionIndex.value ?: 0)
    }

    /**
     * Moves to the next quiz question by incrementing the current question index.
     */
    fun moveToNextQuestion() {
        _currentQuestionIndex.value = (_currentQuestionIndex.value ?: 0) + 1
    }

    /**
     * Checks the selected answer and updates the score if the answer is correct.
     * @param selectedAnswer The index of the selected answer.
     */
    fun answerQuestion(selectedAnswer: Int) {
        val currentQuestion = getCurrentQuestion()
        if (currentQuestion?.correctAnswer == selectedAnswer) {
            _score.value = (_score.value ?: 0) + 1
        }
    }

    /**
     * Adds the current player's score to the leaderboard and saves the updated leaderboard.
     */
    fun addCurrentPlayerToLeaderboard() {
        val currentScore = _score.value ?: 0
        val newEntry = LeaderboardEntry(playerName, currentScore)
        val updatedLeaderboard = (_leaderboard.value ?: emptyList()).toMutableList()
        updatedLeaderboard.add(newEntry)
        updatedLeaderboard.sortByDescending { it.score }
        _leaderboard.value = updatedLeaderboard

        // Save updated leaderboard
        saveLeaderboard()
    }

    /**
     * Resets the quiz by reloading questions and resetting the score and current question index.
     */
    fun resetQuiz() {
        _currentQuestionIndex.value = 0
        _score.value = 0
        loadQuestions()
    }

    /**
     * Data class representing a leaderboard entry.
     * @param name The name of the player.
     * @param score The score of the player.
     */
    data class LeaderboardEntry(val name: String, val score: Int)
}