package com.bylaew.test.seqtask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bylaew.test.seqtask.R
import com.bylaew.test.seqtask.data.model.Movie
import com.bylaew.test.seqtask.databinding.ItemMovieBinding
import java.util.Locale

class MovieAdapter(private val onMovieClick: (Movie) -> Unit) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies: List<Movie> = emptyList()

    fun submitList(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                movieTitle.text = movie.localizedName
                Glide.with(root.context)
                    .load(movie.imageUrl?: R.drawable.no_image)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(movieImage)
            }
            itemView.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }
}