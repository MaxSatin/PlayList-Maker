package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ItunesAPI {
    @GET("/search?{term}")
    fun getSongsList(@Path("term") artistName : String) : Call<TrackListResponse>
}