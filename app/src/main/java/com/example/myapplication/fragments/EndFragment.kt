package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEndBinding
import com.example.myapplication.databinding.ItemLeaderboardBinding
import com.example.myapplication.viewmodels.QuizViewModel

/**
 * EndFragment displays the final score and the leaderboard at the end of the quiz.
 */
class EndFragment : Fragment() {

    // Binding object instance corresponding to the fragment_end.xml layout
    private var _binding: FragmentEndBinding? = null

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
        _binding = FragmentEndBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the player's name and score in the UI
        binding.nameText.text = getString(R.string.jugador, viewModel.playerName)
        binding.scoreText.text = getString(R.string.puntuaci_n, viewModel.score.value.toString())

        // Add current player to leaderboard
        viewModel.addCurrentPlayerToLeaderboard()

        // Set up the leaderboard RecyclerView
        binding.leaderboardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = LeaderboardAdapter(viewModel.playerName)
        binding.leaderboardRecyclerView.adapter = adapter

        // Observe the leaderboard data and update the adapter
        viewModel.leaderboard.observe(viewLifecycleOwner) { leaderboard ->
            adapter.submitList(leaderboard)
        }

        // Set up the try again button to restart the quiz
        binding.tryAgainButton.setOnClickListener {
            findNavController().navigate(R.id.action_endFragment_to_welcomeFragment)
            viewModel.resetQuiz()
        }

        // Set up the exit button to close the app
        binding.exitButton.setOnClickListener {
            requireActivity().finish()
        }
    }

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Adapter class for the leaderboard RecyclerView.
     * @param currentPlayerName The name of the current player.
     */
    private class LeaderboardAdapter(private val currentPlayerName: String) :
        RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

        // List of leaderboard entries
        private var items: List<QuizViewModel.LeaderboardEntry> = emptyList()

        /**
         * Submits a new list of leaderboard entries to the adapter.
         * @param newItems The new list of leaderboard entries.
         */
        fun submitList(newItems: List<QuizViewModel.LeaderboardEntry>) {
            items = newItems
            notifyDataSetChanged()
        }

        /**
         * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
         * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        /**
         * Called by RecyclerView to display the data at the specified position.
         * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val entry = items[position]
            holder.bind(entry, position + 1, entry.name == currentPlayerName)
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         * @return The total number of items in this adapter.
         */
        override fun getItemCount(): Int = items.size

        /**
         * ViewHolder class for the leaderboard items.
         * @param binding The binding object for the leaderboard item layout.
         */
        class ViewHolder(private val binding: ItemLeaderboardBinding) : RecyclerView.ViewHolder(binding.root) {
            /**
             * Binds the leaderboard entry data to the ViewHolder.
             * @param entry The leaderboard entry data.
             * @param position The position of the entry in the leaderboard.
             * @param isCurrentPlayer Whether the entry belongs to the current player.
             */
            fun bind(entry: QuizViewModel.LeaderboardEntry, position: Int, isCurrentPlayer: Boolean) {
                binding.positionText.text = position.toString()
                binding.nameText.text = entry.name
                binding.scoreText.text = entry.score.toString()

                // Highlight the current player's entry
                if (isCurrentPlayer) {
                    itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.leaderboard_item_background)
                    itemView.elevation = 4f
                } else {
                    itemView.background = null
                    itemView.elevation = 0f
                }
            }
        }
    }
}