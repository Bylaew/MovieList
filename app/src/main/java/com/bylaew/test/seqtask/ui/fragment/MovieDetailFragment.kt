package com.bylaew.test.seqtask.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bylaew.test.seqtask.MainActivity
import com.bylaew.test.seqtask.R
import com.bylaew.test.seqtask.databinding.FragmentMovieDetailBinding



class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = args.movie

        movie.name?.let { (requireActivity() as MainActivity).setToolbarTitle(it) }

        with(binding) {
            localizedName.text = movie.localizedName

            year.text = movie.year?.let { "$it год" } ?: "Год неизвестен"

            rating.text = movie.rating?.let { "$it КиноПоиск" } ?: "Нет рейтинга"

            description.text = movie.description ?: "Описание отсутствует"

            genres.text = if (movie.genres.isNotEmpty()) {
                movie.genres.joinToString(", ") + ","
            } else {
                "Жанр неизвестен"
            }


            Glide.with(this@MovieDetailFragment)
                .load(movie.imageUrl?: R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(movieImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}