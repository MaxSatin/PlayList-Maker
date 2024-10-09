package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.player.data.repository.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository
import org.koin.dsl.module

val playerRepositoryModule = module {

    single <MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    factory { MediaPlayer() }

    factory { Gson() }
}