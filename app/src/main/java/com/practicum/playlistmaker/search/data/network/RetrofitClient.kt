package com.practicum.playlistmaker.search.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val ITUNES_BASE_URL = "https://itunes.apple.com"

//    private val client: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://itunes.apple.com")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ItunesAPI::class.java)
//    }
//
//    val iTunesApi: ItunesAPI by lazy {
//        client.create(ItunesAPI::class.java)
//    }
}