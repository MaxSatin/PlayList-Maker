package com.practicum.playlistmaker.medialibrary.di

import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.viewmodel.FavoriteTracksViewModel
import com.practicum.playlistmaker.medialibrary.presentation.playlists.createplaylists.viewmodel.CreatePlayListsViewModel
import com.practicum.playlistmaker.medialibrary.presentation.playlists.edit_playlist.EditPlayListViewModel
import com.practicum.playlistmaker.medialibrary.presentation.playlists.edit_playlist.EditPlaylistDataViewModel
import com.practicum.playlistmaker.medialibrary.presentation.playlists.playlist_details.viewmodel.PlaylistDetailsViewModel
import com.practicum.playlistmaker.medialibrary.presentation.playlists.playlists.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.medialibrary.presentation.root.viewmodel.MediaLibraryViewModel
import com.practicum.playlistmaker.medialibrary.ui.edit_playlist_fragment.EditPlaylistDataFragment
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

    viewModel {
        EditPlayListViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        PlaylistDetailsViewModel(get(), get())
    }

    viewModel {
        EditPlaylistDataViewModel(get(), get())
    }



    single { Gson() }
}