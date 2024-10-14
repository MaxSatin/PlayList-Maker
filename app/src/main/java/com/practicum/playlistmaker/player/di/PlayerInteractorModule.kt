package com.practicum.playlistmaker.player.di

import com.practicum.playlistmaker.player.domain.player_interactor.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.player_interactor.MediaPlayerInteractorImpl
import org.koin.dsl.module

val playerInteractorModule = module {

    factory <MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }
}