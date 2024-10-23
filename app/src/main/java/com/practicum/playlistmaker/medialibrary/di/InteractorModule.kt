package com.practicum.playlistmaker.medialibrary.di

import android.content.Context
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaLibraryInteractorModule = module {

    single <MediaLibraryInteractor> {
        MediaLibraryInteractorImpl(get())
    }
}