package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.dto.NetworkResponse

interface TracklistNetworkClient {
    suspend fun doTrackRequest(dto: Any?) : NetworkResponse
}