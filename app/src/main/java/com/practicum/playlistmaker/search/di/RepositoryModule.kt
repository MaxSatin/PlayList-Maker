package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.data.repository.TracklistRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.impl.DatabaseRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.DatabaseRepository
import com.practicum.playlistmaker.search.domain.repository.TrackListRepository
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    single <TrackListRepository> {
        TracklistRepositoryImpl(get(), get())
    }

    single <TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(get(), get())
    }

    single <DatabaseRepository> {
        DatabaseRepositoryImpl(get())
    }

}