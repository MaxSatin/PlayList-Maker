package com.practicum.playlistmaker.search.domain.repository
import com.practicum.playlistmaker.search.domain.track_model.Resourse
import com.practicum.playlistmaker.search.domain.track_model.Track
import kotlinx.coroutines.flow.Flow

interface TrackListRepository {
    fun getTrackList(expression: String) : Flow<Resourse<List<Track>>>
}