package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.presentation.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::SearchViewModel)

}