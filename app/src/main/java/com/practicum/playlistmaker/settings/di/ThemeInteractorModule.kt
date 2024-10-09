package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.domain.interactor.AppThemeInteractor
import com.practicum.playlistmaker.settings.domain.interactor.AppThemeInteractorImpl
import org.koin.dsl.module

val themeInteractorModule = module {

    single <AppThemeInteractor> {
        AppThemeInteractorImpl(get())
    }
}
