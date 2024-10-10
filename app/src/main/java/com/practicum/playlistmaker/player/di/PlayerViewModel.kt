package com.practicum.playlistmaker.player.di

import android.app.Application
import com.practicum.playlistmaker.player.presentation.view_model.PlayerViewModel
import com.practicum.playlistmaker.player.ui.PlayerActivity
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    factory { (track: String) ->
            PlayerViewModel(
                androidApplication(), track, get(), get()
            )

    }
}