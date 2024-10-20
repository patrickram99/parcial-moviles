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
import com.example.myapplication.databinding.FragmentWelcomeBinding
import com.example.myapplication.viewmodels.QuizViewModel

/**
 * WelcomeFragment is the initial fragment that allows the user to enter their name and start the quiz.
 */
class WelcomeFragment : Fragment() {

    // Binding object instance corresponding to the fragment_welcome.xml layout
    private var _binding: FragmentWelcomeBinding? = null

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
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Quiz Worlds 2024"

        // Set the player's name in the EditText from the ViewModel
        binding.nameEditText.setText(viewModel.playerName)

        // Set up the start button to navigate to the question fragment
        binding.startButton.setOnClickListener {
            viewModel.playerName = binding.nameEditText.text.toString()
            findNavController().navigate(R.id.action_welcomeFragment_to_questionFragment)
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