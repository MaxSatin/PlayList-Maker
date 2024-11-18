package com.practicum.playlistmaker.search.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.network.ItunesAPI
import com.practicum.playlistmaker.search.data.network.TracklistRetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository.TracklistNetworkClient
import org.koin.android.ext.koin.androidContext
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

    single {
        androidContext().getSharedPreferences(
            "history_track_list_shared_prefs",
            Context.MODE_PRIVATE
        )
    }

    single { Gson() }

    single<TracklistNetworkClient> {
        TracklistRetrofitNetworkClient(androidContext(),get())
    }

}