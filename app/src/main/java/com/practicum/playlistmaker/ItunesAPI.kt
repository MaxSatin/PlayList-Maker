package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    fun getSongsList(@Query("term") artistName : String) : Call<TrackListResponse>
}