package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.model.Track

interface TracklistInteractor {
    fun loadTracks(consumer: Consumer<List<Track>>)
}