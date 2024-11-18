package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TrackListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    suspend fun getSongsList(@Query("term") artistName : String) : TrackListResponse
}