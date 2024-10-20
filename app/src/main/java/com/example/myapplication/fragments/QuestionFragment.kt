package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentQuestionBinding
import com.example.myapplication.viewmodels.QuizViewModel

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Pregunta ${viewModel.currentQuestionIndex.value?.plus(1)} de 5"
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentQuestion()?.let { question ->
            binding.questionText.text = question.question
            binding.option1.text = question.options[0]
            binding.option2.text = question.options[1]
            binding.option3.text = question.options[2]
            binding.option4.text = question.options[3]

            binding.checkButton.setOnClickListener {
                val selectedAnswer = when {
                    binding.option1.isChecked -> 0
                    binding.option2.isChecked -> 1
                    binding.option3.isChecked -> 2
                    binding.option4.isChecked -> 3
                    else -> -1
                }

                if (selectedAnswer != -1) {
                    viewModel.answerQuestion(selectedAnswer)
                    val bundle = Bundle().apply {
                        putInt("selectedAnswer", selectedAnswer)
                    }
                    findNavController().navigate(R.id.action_questionFragment_to_answerFragment, bundle)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}