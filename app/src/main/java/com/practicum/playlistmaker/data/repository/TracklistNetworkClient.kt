package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.NetworkResponse

interface TracklistNetworkClient {
    fun doTrackRequest(dto: Any?): NetworkResponse
}