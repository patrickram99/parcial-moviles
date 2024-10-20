package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAnswerBinding
import com.example.myapplication.viewmodels.QuizViewModel

class AnswerFragment : Fragment() {

    private var _binding: FragmentAnswerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAnswerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedAnswer = arguments?.getInt("selectedAnswer") ?: -1
        val currentQuestion = viewModel.getCurrentQuestion()

        if (currentQuestion != null) {
            val isCorrect = selectedAnswer == currentQuestion.correctAnswer
            binding.resultText.text = if (isCorrect) "¡Correcto!" else "Incorrecto"
            binding.explanationText.text = currentQuestion.explanation

            if (isCorrect) {
                binding.resultImage.setImageResource(R.drawable.correct_answer)
            } else {
                binding.resultImage.setImageResource(R.drawable.incorrect_answer)
            }
        }

        binding.nextButton.setOnClickListener {
            viewModel.moveToNextQuestion()
            if (viewModel.currentQuestionIndex.value!! < viewModel.questions.value!!.size) {
                findNavController().navigate(R.id.action_answerFragment_to_questionFragment)
            } else {
                val bundle = Bundle().apply {
                    putInt("finalScore", viewModel.score.value ?: 0)
                    putInt("totalQuestions", viewModel.questions.value?.size ?: 0)
                }
                findNavController().navigate(R.id.action_answerFragment_to_endFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}