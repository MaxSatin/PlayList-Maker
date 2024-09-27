package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.track_model.Track

interface TracksHistoryRepository {
    fun saveTracksHistoryToLocalStorage(data: List<Track>)
    fun getTracks(): List<Track>
    fun clearStorage()
    fun isHistoryEmpty(): Boolean
}