package com.bylaew.test.seqtask.di

import com.bylaew.test.seqtask.data.api.MovieApiService
import com.bylaew.test.seqtask.data.api.RetrofitClient
import com.bylaew.test.seqtask.data.repository.MovieRepository
import com.bylaew.test.seqtask.ui.viewmodel.MovieViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::MovieRepository)
    viewModelOf(::MovieViewModel)
    single<MovieApiService> { RetrofitClient.movieApiService }
}