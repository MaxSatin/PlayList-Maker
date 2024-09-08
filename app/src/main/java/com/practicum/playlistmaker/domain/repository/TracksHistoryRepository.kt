package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.Track

interface TracksHistoryRepository {
    fun getTracks(): List<Track>
}