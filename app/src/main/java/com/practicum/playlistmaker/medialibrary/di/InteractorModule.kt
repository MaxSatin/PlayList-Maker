package com.practicum.playlistmaker.medialibrary.di


import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractorImpl
import org.koin.dsl.module

val mediaLibraryInteractorModule = module {

    single <MediaLibraryInteractor> {
        MediaLibraryInteractorImpl(get())
    }
}