package com.practicum.playlistmaker.data.storage.manipulator

import com.practicum.playlistmaker.domain.model.Track

interface SaveTrackHistoryToStorage {
    fun saveTracksHistoryToLocalStorage(data: List<Track>)
}