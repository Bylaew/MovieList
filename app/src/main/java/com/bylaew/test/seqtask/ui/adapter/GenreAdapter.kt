package com.bylaew.test.seqtask.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bylaew.test.seqtask.databinding.ItemGenreBinding
import java.util.Locale


class GenreAdapter(private val onGenreSelected: (String?) -> Unit) :
    RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var genres: List<String> = emptyList()
    private var selectedGenre: String? = null

    fun submitList(newGenres: List<String>) {
        genres = newGenres
        notifyDataSetChanged()
    }

    fun selectGenre(genre: String?) {
        val oldSelectedPosition = genres.indexOf(selectedGenre)
        val newSelectedPosition = genres.indexOf(genre)

        selectedGenre = genre

        if (oldSelectedPosition != -1) notifyItemChanged(oldSelectedPosition)
        if (newSelectedPosition != -1) notifyItemChanged(newSelectedPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount() = genres.size

    inner class GenreViewHolder(private val binding: ItemGenreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: String) {
            binding.genreTitle.text = genre.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            if (genre == selectedGenre) {
                binding.root.setBackgroundColor(Color.parseColor("#FFC967"))
            } else {
                binding.root.setBackgroundColor(Color.TRANSPARENT)
            }
            binding.root.setOnClickListener {
                if (genre == selectedGenre) {
                    onGenreSelected(null)
                    selectGenre(null)
                } else {
                    onGenreSelected(genre)
                    selectGenre(genre)
                }
            }
        }
    }
}