package com.practicum.playlistmaker.Creator

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.data.Constants
import com.practicum.playlistmaker.data.network.TracklistRetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.TracklistRepositoryImpl
import com.practicum.playlistmaker.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.data.storage.impl.GetTracksFromStorageImpl
import com.practicum.playlistmaker.data.storage.impl.SaveTracksHistoryToStorageImpl
import com.practicum.playlistmaker.data.storage.manipulator.GetTracksHistoryFromStorage
import com.practicum.playlistmaker.data.storage.manipulator.SaveTrackHistoryToStorage
import com.practicum.playlistmaker.domain.interactors.AddTrackToHistoryIntr
import com.practicum.playlistmaker.domain.interactors.SearchTrackListIntr
import com.practicum.playlistmaker.domain.repository.TrackListRepository
import com.practicum.playlistmaker.domain.use_case.tracks_intr.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_intr.GetTrackListFromServerUseCase

object Creator {

    fun provideGson(): Gson {
        return Gson()
    }

    fun provideSharedPrefsClient(context: Context, key: String): SharedPrefsClient {
        return SharedPrefsClient(context, key)
    }

    fun provideSaveTrackHistoryToStorage(context: Context): SaveTrackHistoryToStorage{
        return SaveTracksHistoryToStorageImpl(provideSharedPrefsClient(
            context,
            Constants.SHAREDPREFS_TRACKS_HISTORY)
        )
    }

    fun provideGetTracksHistoryFromStorage(context: Context): GetTracksHistoryFromStorage {
        return GetTracksFromStorageImpl(provideSharedPrefsClient(
            context,
            Constants.SHAREDPREFS_TRACKS_HISTORY)
        )
    }

    fun provideTrackListRepository(): TrackListRepository{
        return TracklistRepositoryImpl(TracklistRetrofitNetworkClient())
    }

    fun provideSearchTrackListIntr(): SearchTrackListIntr{
        return GetTrackListFromServerUseCase(provideTrackListRepository())

    }

    fun provideAddTrackToHistoryIntr(context: Context): AddTrackToHistoryIntr{
        return AddTrackToHistoryUseCase(
            provideSaveTrackHistoryToStorage(context),
            provideGetTracksHistoryFromStorage(context)
        )
    }
}