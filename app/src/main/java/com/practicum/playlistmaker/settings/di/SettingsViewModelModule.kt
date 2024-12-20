package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.presentation.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsViewModelModule = module {
    viewModel {
        SettingsViewModel(
            androidApplication(), get(), get()
        )
    }
}