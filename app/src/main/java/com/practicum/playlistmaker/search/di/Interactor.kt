package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.domain.database_interactor.DatabaseInteractor
import com.practicum.playlistmaker.search.domain.database_interactor.DatabaseInteractorImpl
import com.practicum.playlistmaker.search.domain.tracks_intr.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.CheckIsHistoryEmptyUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.ClearHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackHistoryFromStorageUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackListFromServerUseCase
import org.koin.dsl.module

val interactorModule = module {

    factory {
        AddTrackToHistoryUseCase(get())
    }

    factory {
        CheckIsHistoryEmptyUseCase(get())
    }

    factory{
        ClearHistoryUseCase(get())
    }

    factory {
        GetTrackHistoryFromStorageUseCase(get())
    }

    factory {
        GetTrackListFromServerUseCase(get())
    }

    factory <DatabaseInteractor> {
        DatabaseInteractorImpl(get())
    }
}