package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.data.repository.TracklistRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.TrackListRepository
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.search.domain.tracks_intr.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.CheckIsHistoryEmptyUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.ClearHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackHistoryFromStorageUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackListFromServerUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single <TrackListRepository> {
        TracklistRepositoryImpl(get())
    }

//    single <TracksHistoryRepository> {
//        TracksHistoryRepositoryImpl(get(), get(named("sharedPrefsHistory")))
//    }
    single <TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(get(), get())
    }


}