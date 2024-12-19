package com.practicum.playlistmaker.medialibrary.di

import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.viewmodel.FavoriteTracksViewModel
import com.practicum.playlistmaker.medialibrary.presentation.playlists.createplaylists.viewmodel.CreatePlayListsViewModel
import com.practicum.playlistmaker.medialibrary.presentation.root.viewmodel.MediaLibraryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryViewModelModule = module {
    viewModel {
        MediaLibraryViewModel(get())
    }

    viewModel {
        FavoriteTracksViewModel(get(), get())
    }

    viewModel {
        CreatePlayListsViewModel(get())
    }

    single { Gson() }
}