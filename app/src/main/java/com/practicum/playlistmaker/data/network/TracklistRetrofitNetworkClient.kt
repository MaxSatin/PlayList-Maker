package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.NetworkResponse
import com.practicum.playlistmaker.data.dto.TrackListRequest
import com.practicum.playlistmaker.data.repository.TracklistNetworkClient

class TracklistRetrofitNetworkClient : TracklistNetworkClient {
    override fun doTrackRequest(dto: Any?): NetworkResponse {
        return if (dto is TrackListRequest) {
            val response = RetrofitClient.iTunesApi.getSongsList(dto.expression).execute()
            val networkResponse = response.body() ?: NetworkResponse()

            networkResponse.apply { resultCode = response.code() }
        } else {
            NetworkResponse().apply { resultCode = 400 }

        }
    }
}







