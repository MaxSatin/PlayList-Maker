package com.practicum.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val iTunesBaseUrl = "https://itunes.apple.com"

    private val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val iTunesApi: ItunesAPI by lazy {
        client.create(ItunesAPI::class.java)
    }
}