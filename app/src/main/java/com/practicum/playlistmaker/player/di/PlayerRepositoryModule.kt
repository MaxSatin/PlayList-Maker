package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.AppDatabase
import com.practicum.playlistmaker.player.data.repository.DatabaseRepositoryImpl
import com.practicum.playlistmaker.player.data.repository.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.data.utils.TrackDbConverter
import com.practicum.playlistmaker.player.domain.repository.DatabaseRepository
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val playerRepositoryModule = module {

    factory <MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<DatabaseRepository> {
        DatabaseRepositoryImpl(get(), get())
    }

    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "database.db")
            .build()
    }

    factory { TrackDbConverter() }

    factory { MediaPlayer() }
}