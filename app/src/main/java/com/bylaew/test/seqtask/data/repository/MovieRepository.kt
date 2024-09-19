package com.bylaew.test.seqtask.data.repository

import com.bylaew.test.seqtask.data.api.MovieApiService
import com.bylaew.test.seqtask.data.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MovieRepository (
    private val movieApiService: MovieApiService
) {
    suspend fun getMovies(): Result<List<Movie>> = withContext(Dispatchers.IO) {
        try {
            val response = movieApiService.getMovies()
            Result.success(response.films)
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка сервера: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка подключения сети"))
        } catch (e: Exception) {
            Result.failure(Exception("Неизвестная ошибка: ${e.message}"))
        }
    }

    fun getAllGenres(movies: List<Movie>): List<String> {
        return movies.flatMap { it.genres }.distinct().sorted()
    }
}