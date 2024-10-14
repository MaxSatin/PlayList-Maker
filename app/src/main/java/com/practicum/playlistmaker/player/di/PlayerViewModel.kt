package com.practicum.playlistmaker.player.di

import com.practicum.playlistmaker.player.presentation.view_model.PlayerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel { (track: String) ->
            PlayerViewModel(
                androidApplication(), track, get(), get()
            )
    }
}