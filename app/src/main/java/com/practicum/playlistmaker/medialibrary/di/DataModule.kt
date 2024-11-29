package com.practicum.playlistmaker.medialibrary.di

import android.content.Context
import com.practicum.playlistmaker.medialibrary.data.repository.MediaLibraryRepositoryImpl
import com.practicum.playlistmaker.medialibrary.domain.repository.MediaLibraryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaLibraryDataModule = module{
    single {
        androidContext().getSharedPreferences(
            "medialibrary_shared_prefs",
            Context.MODE_PRIVATE
        )
    }

    single<MediaLibraryRepository> {
        MediaLibraryRepositoryImpl(get(), get(), get(), get())
    }
}