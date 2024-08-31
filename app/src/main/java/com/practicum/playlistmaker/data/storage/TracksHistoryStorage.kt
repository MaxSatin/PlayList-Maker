package com.practicum.playlistmaker.data.storage

import com.practicum.playlistmaker.CurrentTrack
import com.practicum.playlistmaker.domain.model.Track

interface TracksHistoryStorage {

    fun saveTracksHistoryToLocalStorage(tracks: List<Track>)

    fun getTracksHistoryFromLocalStorage(): List<CurrentTrack>?
}