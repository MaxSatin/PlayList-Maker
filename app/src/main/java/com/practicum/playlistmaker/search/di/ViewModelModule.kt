package com.practicum.playlistmaker.search.di

import android.app.Application
import com.practicum.playlistmaker.search.presentation.view_model.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{
        SearchViewModel(Application(), get(), get(), get(), get(), get())
    }
}