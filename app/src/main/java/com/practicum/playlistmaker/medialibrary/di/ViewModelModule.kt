package com.practicum.playlistmaker.medialibrary.di

import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.MediaLibraryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryViewModelModule = module {
    viewModel {
        MediaLibraryViewModel(get())
    }
}