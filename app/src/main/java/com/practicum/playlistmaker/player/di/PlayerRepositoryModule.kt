package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.player.data.repository.DatabaseRepositoryImpl
import com.practicum.playlistmaker.player.data.repository.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.data.utils.TrackDbConverter
import com.practicum.playlistmaker.player.domain.repository.DatabaseRepository
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository
import org.koin.dsl.module

val playerRepositoryModule = module {

    factory <MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<DatabaseRepository> {
        DatabaseRepositoryImpl(get(), get())
    }

    factory { TrackDbConverter() }

    factory { MediaPlayer() }
}