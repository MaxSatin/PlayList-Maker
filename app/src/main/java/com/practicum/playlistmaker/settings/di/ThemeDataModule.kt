package com.practicum.playlistmaker.settings.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val themeDataModule = module {

    single {
        androidContext()
            .getSharedPreferences(
                "app_theme",
                Context.MODE_PRIVATE)
    }
}