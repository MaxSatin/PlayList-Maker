package com.practicum.playlistmaker.medialibrary.di

import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.FavoriteTracksViewModel
import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.MediaLibraryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryViewModelModule = module {
    viewModel {
        MediaLibraryViewModel(get())
    }

    viewModel {
        FavoriteTracksViewModel(get(), get())
    }

    single { Gson() }
}