package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.data.storage.TracksHistoryStorageManager
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracksHistoryInteractor

class SaveTrackToHistoryUseCase(
    private val tracksHistoryStorageManager: TracksHistoryStorageManager
) : TracksHistoryInteractor {
    override fun searchTracks(trackItem: Track) {
        tracksHistoryStorageManager.saveTracksHistoryToLocalStorage(trackItem)
    }
}