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

class EndFragment : Fragment() {

    private var _binding: FragmentEndBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEndBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameText.text = "Jugador: ${viewModel.playerName}"
        binding.scoreText.text = "Puntuación: ${viewModel.score.value}"

        // Add current player to leaderboard
        viewModel.addCurrentPlayerToLeaderboard()

        binding.leaderboardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = LeaderboardAdapter(viewModel.playerName)
        binding.leaderboardRecyclerView.adapter = adapter

        viewModel.leaderboard.observe(viewLifecycleOwner) { leaderboard ->
            adapter.submitList(leaderboard)
        }

        binding.tryAgainButton.setOnClickListener {
            findNavController().navigate(R.id.action_endFragment_to_welcomeFragment)
            viewModel.resetQuiz()
        }

        binding.exitButton.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class LeaderboardAdapter(private val currentPlayerName: String) :
        RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

        private var items: List<QuizViewModel.LeaderboardEntry> = emptyList()

        fun submitList(newItems: List<QuizViewModel.LeaderboardEntry>) {
            items = newItems
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val entry = items[position]
            holder.bind(entry, position + 1, entry.name == currentPlayerName)
        }

        override fun getItemCount(): Int = items.size

        class ViewHolder(private val binding: ItemLeaderboardBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(entry: QuizViewModel.LeaderboardEntry, position: Int, isCurrentPlayer: Boolean) {
                binding.positionText.text = position.toString()
                binding.nameText.text = entry.name
                binding.scoreText.text = entry.score.toString()

                if (isCurrentPlayer) {
                    itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.leaderboard_item_background)
                    itemView.elevation = 4f // Add a slight elevation for a "lifted" effect
                } else {
                    itemView.background = null
                    itemView.elevation = 0f
                }
            }
        }
    }
}