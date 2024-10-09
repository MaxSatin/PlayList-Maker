package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.NetworkResponse
import com.practicum.playlistmaker.search.data.dto.TrackListRequest
import com.practicum.playlistmaker.search.data.repository.TracklistNetworkClient

class TracklistRetrofitNetworkClient(
    private val iTunesApi: ItunesAPI
) : TracklistNetworkClient {
    override fun doTrackRequest(dto: Any?): NetworkResponse {
        return if (dto is TrackListRequest) {
            try {
//                val response = RetrofitClient.iTunesApi.getSongsList(dto.expression).execute()
                val response = iTunesApi.getSongsList(dto.expression).execute()
                val body = response.body() ?: NetworkResponse()
                return body.apply {
                    resultCode = response.code()
                }
            } catch (ex: Exception) {
                NetworkResponse().apply { resultCode = 503 }
            }
        } else
            return NetworkResponse().apply { resultCode = 400 }
    }
}





