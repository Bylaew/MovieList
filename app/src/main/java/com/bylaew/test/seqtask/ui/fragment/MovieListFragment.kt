package com.bylaew.test.seqtask.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bylaew.test.seqtask.R
import com.bylaew.test.seqtask.databinding.FragmentMovieListBinding
import com.bylaew.test.seqtask.ui.adapter.GenreAdapter
import com.bylaew.test.seqtask.ui.adapter.MovieAdapter
import com.bylaew.test.seqtask.ui.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar

import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val movieViewModel: MovieViewModel by viewModel()
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Фильмы"
        setupGenreRecyclerView()
        setupMovieRecyclerView()
        observeViewModel()
    }


    private fun setupGenreRecyclerView() {
        genreAdapter = GenreAdapter { genre ->
            movieViewModel.selectGenre(genre)
            scrollToMovies()
        }
        binding.genresRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = genreAdapter
        }
    }

    private fun setupMovieRecyclerView() {
        movieAdapter = MovieAdapter { movie ->
            val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie)
            findNavController().navigate(action)
        }
        binding.moviesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = movieAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.uiState.collect { state ->
                updateUI(state)
            }
        }
    }
    private fun updateUI(state: MovieViewModel.UiState) {
        when (state) {
            is MovieViewModel.UiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.moviesTitle.visibility = View.GONE
                binding.genresTitle.visibility = View.GONE
                binding.genresRecyclerView.visibility = View.GONE
                binding.moviesRecyclerView.visibility = View.GONE
            }
            is MovieViewModel.UiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.moviesTitle.visibility = View.VISIBLE
                binding.genresTitle.visibility = View.VISIBLE
                binding.genresRecyclerView.visibility = View.VISIBLE
                binding.moviesRecyclerView.visibility = View.VISIBLE
                genreAdapter.submitList(state.genres)
                genreAdapter.selectGenre(state.selectedGenre)
                movieAdapter.submitList(state.movies)
            }
            is MovieViewModel.UiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.moviesTitle.visibility = View.GONE
                binding.genresTitle.visibility = View.GONE
                binding.genresRecyclerView.visibility = View.GONE
                binding.moviesRecyclerView.visibility = View.GONE
                showErrorSnackbar(state.message)
            }
        }
    }
    private fun showErrorSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("ПОВТОРИТЬ") {
            movieViewModel.loadMovies()
        }
        snackbar.setActionTextColor(Color.parseColor("#FFC967"))
        snackbar.show()
    }

    private fun scrollToMovies() {
        binding.root.post {
            binding.nestedScrollView.smoothScrollTo(0, binding.moviesTitle.top)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}