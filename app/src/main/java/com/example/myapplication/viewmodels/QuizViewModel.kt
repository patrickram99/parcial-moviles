package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.models.QuizQuestion
import org.json.JSONArray
import java.util.*

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val _questions = MutableLiveData<List<QuizQuestion>>()
    val questions: LiveData<List<QuizQuestion>> = _questions

    private val _currentQuestionIndex = MutableLiveData<Int>()
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    init {
        loadQuestions()
        _currentQuestionIndex.value = 0
        _score.value = 0
    }

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

        // Seleccionar 5 preguntas al azar
        val randomQuestions = mutableListOf<QuizQuestion>()
        val random = Random()
        for (i in 0 until 5) {
            val index = random.nextInt(questionList.size)
            randomQuestions.add(questionList[index])
            questionList.removeAt(index)
        }

        _questions.value = randomQuestions
    }

    fun getCurrentQuestion(): QuizQuestion? {
        return questions.value?.getOrNull(currentQuestionIndex.value ?: 0)
    }

    fun moveToNextQuestion() {
        _currentQuestionIndex.value = (_currentQuestionIndex.value ?: 0) + 1
    }

    fun answerQuestion(selectedAnswer: Int) {
        val currentQuestion = getCurrentQuestion()
        if (currentQuestion?.correctAnswer == selectedAnswer) {
            _score.value = (_score.value ?: 0) + 1
        }
    }

    fun resetQuiz() {
        _currentQuestionIndex.value = 0
        _score.value = 0
        loadQuestions() // Recargar las preguntas al azar
    }
}