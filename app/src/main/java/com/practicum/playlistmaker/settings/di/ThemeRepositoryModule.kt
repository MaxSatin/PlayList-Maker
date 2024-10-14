package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.data.repository.AppThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.repository.AppThemeRepository
import org.koin.dsl.module

val themeRepositoryModule = module {

    single <AppThemeRepository> {
        AppThemeRepositoryImpl(get())
    }

}