package com.practicum.playlistmaker.domain.repository


import com.practicum.playlistmaker.domain.model.Resourse
import com.practicum.playlistmaker.domain.model.Track

interface TrackListRepository {
    fun getTrackList(expression: String) : Resourse<List<Track>>
}