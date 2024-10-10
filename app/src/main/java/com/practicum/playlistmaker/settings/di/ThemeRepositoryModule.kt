package com.practicum.playlistmaker.settings.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.settings.data.repository.AppThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.repository.AppThemeRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val themeRepositoryModule = module {

    single <AppThemeRepository> {
        AppThemeRepositoryImpl(get(named("sharedPrefsAppTheme")))
    }

}