package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentQuestion()?.let { question ->
            binding.questionText.text = question.question
            binding.option1.text = question.options[0]
            binding.option2.text = question.options[1]
            binding.option3.text = question.options[2]
            binding.option4.text = question.options[3]

            val options = listOf(binding.option1, binding.option2, binding.option3, binding.option4)

            options.forEachIndexed { index, button ->
                button.setOnClickListener {
                    viewModel.answerQuestion(index)
                    val bundle = Bundle().apply {
                        putInt("selectedAnswer", index)
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