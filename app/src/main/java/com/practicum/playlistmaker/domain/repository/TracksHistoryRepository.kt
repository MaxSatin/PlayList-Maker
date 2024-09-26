package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.Track

interface TracksHistoryRepository {
    fun saveTracksHistoryToLocalStorage(data: List<Track>)
    fun getTracks(): List<Track>
    fun clearStorage()
    fun isHistoryEmpty(): Boolean
}