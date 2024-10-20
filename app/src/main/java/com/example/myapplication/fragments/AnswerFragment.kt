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
import com.example.myapplication.databinding.FragmentAnswerBinding
import com.example.myapplication.viewmodels.QuizViewModel

/**
 * AnswerFragment displays the result of the user's answer to a quiz question.
 */
class AnswerFragment : Fragment() {

    // Binding object instance corresponding to the fragment_answer.xml layout
    private var _binding: FragmentAnswerBinding? = null

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
        _binding = FragmentAnswerBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.score, viewModel.score.value.toString(), viewModel.questions.value?.size.toString())

        val selectedAnswer = arguments?.getInt("selectedAnswer") ?: -1
        val currentQuestion = viewModel.getCurrentQuestion()

        if (currentQuestion != null) {
            val isCorrect = selectedAnswer == currentQuestion.correctAnswer
            binding.resultText.text = if (isCorrect) getString(R.string.correcto) else getString(R.string.incorrecto)
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

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}