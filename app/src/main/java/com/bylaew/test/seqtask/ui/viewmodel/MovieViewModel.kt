package com.bylaew.test.seqtask.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bylaew.test.seqtask.data.model.Movie
import com.bylaew.test.seqtask.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MovieViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var selectedGenre: String? = null

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.getMovies()
            result.fold(
                onSuccess = { movies ->
                    val genres = repository.getAllGenres(movies)
                    _uiState.value = UiState.Success(genres, filterMovies(movies), selectedGenre)
                },
                onFailure = { exception ->
                    _uiState.value = UiState.Error(exception.message ?: "Неизвестная ошибка")
                }
            )
        }
    }

    fun selectGenre(genre: String?) {
        selectedGenre = if (selectedGenre == genre) null else genre
        (_uiState.value as? UiState.Success)?.let { currentState ->
            _uiState.value = currentState.copy(
                movies = filterMovies(currentState.allMovies),
                selectedGenre = selectedGenre
            )
        }
    }

    private fun filterMovies(movies: List<Movie>): List<Movie> {
        return if (selectedGenre == null) {
            movies
        } else {
            movies.filter { it.genres.contains(selectedGenre) }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(
            val genres: List<String>,
            val movies: List<Movie>,
            val selectedGenre: String?,
            val allMovies: List<Movie> = movies
        ) : UiState()
        data class Error(val message: String) : UiState()
    }
}