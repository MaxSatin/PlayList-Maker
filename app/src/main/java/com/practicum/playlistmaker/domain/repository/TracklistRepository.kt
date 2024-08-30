package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.data.dto.NetworkResponse
import com.practicum.playlistmaker.domain.model.Resourse
import com.practicum.playlistmaker.domain.model.Track

interface TracklistRepository {
    fun doTrackRequest(expression: String): Resourse<List<Track>>
}