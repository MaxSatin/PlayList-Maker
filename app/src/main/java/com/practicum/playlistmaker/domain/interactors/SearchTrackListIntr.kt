package com.practicum.playlistmaker.domain.interactors

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.model.Track

interface SearchTrackListIntr {
    fun searchTracks(expression: String, consumer: Consumer<List<Track>>)
}