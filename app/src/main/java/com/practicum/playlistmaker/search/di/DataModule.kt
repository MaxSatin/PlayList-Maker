package com.practicum.playlistmaker.search.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.network.ItunesAPI
import com.practicum.playlistmaker.search.data.network.TracklistRetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository.TracklistNetworkClient
import com.practicum.playlistmaker.search.data.repository.TracklistRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.TrackListRepository
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.search.domain.tracks_intr.AddTrackToHistoryUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ItunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesAPI::class.java)
    }

    single(named("sharedPrefsHistory")) {
        androidContext().getSharedPreferences(
            "history_track_list_shared_prefs",
            Context.MODE_PRIVATE
        )
    }

//    single(named("sharedPrefsAppTheme")) {
//        androidContext().getSharedPreferences(
//            "app_theme",
//            Context.MODE_PRIVATE
//        )
//    }

    factory { Gson() }

    single<TracklistNetworkClient> {
        TracklistRetrofitNetworkClient(get())
    }

//    single <TrackListRepository> {
//        TracklistRepositoryImpl(get())
//    }
//
//    single <TracksHistoryRepository> {
//        TracksHistoryRepositoryImpl(get(), get(named("sharedPrefsHistory")))
//    }

}