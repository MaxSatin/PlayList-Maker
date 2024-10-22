package com.practicum.playlistmaker.medialibrary.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaLibraryInteractorModule = module{
    single {
        androidContext().getSharedPreferences(
            "medialibrary_shared_prefs",
            Context.MODE_PRIVATE
        )
    }