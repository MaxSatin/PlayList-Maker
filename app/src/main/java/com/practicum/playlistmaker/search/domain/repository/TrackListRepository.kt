package com.practicum.playlistmaker.search.domain.repository
import com.practicum.playlistmaker.search.domain.track_model.Resourse
import com.practicum.playlistmaker.search.domain.track_model.Track

interface TrackListRepository {
    fun getTrackList(expression: String) : Resourse<List<Track>>
}