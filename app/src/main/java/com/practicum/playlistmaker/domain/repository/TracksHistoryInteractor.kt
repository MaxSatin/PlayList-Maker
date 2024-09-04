package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.Track

interface TracksHistoryInteractor {
    fun getTrackHistoryList()
    fun addTracksToHistory(track: Track)
}