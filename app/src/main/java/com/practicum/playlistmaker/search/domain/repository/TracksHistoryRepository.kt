package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.track_model.Track
import kotlinx.coroutines.flow.Flow

interface TracksHistoryRepository {
    fun saveTracksHistoryToLocalStorage(data: List<Track>)
    fun getTracks(): Flow<List<Track>>
    fun clearStorage()
//    fun isHistoryEmpty(): Boolean
}