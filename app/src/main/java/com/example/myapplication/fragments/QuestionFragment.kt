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

/**
 * QuestionFragment displays a quiz question and its possible answers.
 */
class QuestionFragment : Fragment() {

    // Binding object instance corresponding to the fragment_question.xml layout
    private var _binding: FragmentQuestionBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    // Shared ViewModel instance to manage quiz data
    private val viewModel: QuizViewModel by activityViewModels()

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.question_title, viewModel.currentQuestionIndex.value?.plus(1))
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

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}