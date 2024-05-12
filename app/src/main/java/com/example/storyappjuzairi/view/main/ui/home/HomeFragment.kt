package com.example.storyappjuzairi.view.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.data.pref.UserPreference
import com.example.storyappjuzairi.data.pref.dataStore
import com.example.storyappjuzairi.databinding.FragmentHomeBinding
import com.example.storyappjuzairi.view.main.ui.adapter.StoryAdapter
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: HomeViewModelFactory = HomeViewModelFactory.getInstance(requireContext())
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        val storyAdapter = StoryAdapter()

        if (homeViewModel.story.value == null) {
            homeViewModel.findStory()
        }

        homeViewModel.story.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val storyData = result.data
                        storyAdapter.submitList(storyData)
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(
                            view,
                            "Gagal memuat cerita",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = storyAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager(requireActivity()).orientation
                )
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}