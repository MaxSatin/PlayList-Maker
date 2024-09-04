package com.practicum.playlistmaker.data.storage

import com.practicum.playlistmaker.CurrentTrack
import com.practicum.playlistmaker.domain.model.Track

interface TracksHistoryStorageManager<T> {

    fun saveTracksHistoryToLocalStorage(data: List<T>)

    fun getTracksHistoryFromLocalStorage(): List<T>?
}