package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.NetworkResponse
import com.practicum.playlistmaker.data.dto.TrackListResponse
import retrofit2.Callback

interface TracklistNetworkClient {
    fun doTrackRequest(dto: Any?) : NetworkResponse
}