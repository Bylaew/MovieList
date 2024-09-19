package com.bylaew.test.seqtask.data.api

import com.bylaew.test.seqtask.data.model.MovieResponse
import retrofit2.http.GET

interface MovieApiService {
    @GET("films.json")
    suspend fun getMovies(): MovieResponse
}