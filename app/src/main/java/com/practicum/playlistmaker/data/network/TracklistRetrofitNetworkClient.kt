package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.NetworkResponse
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.data.dto.TrackListRequest
import com.practicum.playlistmaker.data.dto.TrackListResponse
import com.practicum.playlistmaker.data.repository.TracklistNetworkClient
import retrofit2.Callback

class TracklistRetrofitNetworkClient : TracklistNetworkClient {
    override fun doTrackRequest(dto: Any?): NetworkResponse {
        return if (dto is TrackListRequest) {
            try {
                val response = RetrofitClient.iTunesApi.getSongsList(dto.expression).execute()
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





