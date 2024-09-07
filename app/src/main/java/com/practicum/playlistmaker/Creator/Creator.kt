package com.practicum.playlistmaker.Creator

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.data.network.TracklistRetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.TracklistRepositoryImpl
import com.practicum.playlistmaker.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.data.storage.impl.GetTracksFromStorageImpl
import com.practicum.playlistmaker.data.storage.impl.SaveTracksHistoryToStorageImpl
import com.practicum.playlistmaker.data.storage.manipulator.GetTracksHistoryFromStorage
import com.practicum.playlistmaker.data.storage.manipulator.SaveTrackHistoryToStorage
import com.practicum.playlistmaker.domain.repository.TrackListRepository

object Creator {

    fun provideGson(): Gson {
        return Gson()
    }

    fun provideSharedPrefsClient(context: Context, key: String): SharedPrefsClient {
        return SharedPrefsClient(context, key)
    }

    fun provideSaveTrackHistoryToStorage(context: Context, key: String): SaveTrackHistoryToStorage{
        return SaveTracksHistoryToStorageImpl(provideSharedPrefsClient(context, key))
    }

    fun provideGetTracksHistoryFromStorage(context: Context, key: String): GetTracksHistoryFromStorage {
        return GetTracksFromStorageImpl(provideSharedPrefsClient(context, key))
    }

    fun provideTrackListRepository(): TrackListRepository{
        return TracklistRepositoryImpl(TracklistRetrofitNetworkClient())
    }

}