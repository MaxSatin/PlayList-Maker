package com.practicum.playlistmaker.domain.use_case.tracks_intr

import com.practicum.playlistmaker.data.storage.manipulator.GetTracksHistoryFromStorage
import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.domain.model.Track

class GetTrackHistoryFromStorageUseCase(
    private val tracksHistoryStorage: GetTracksHistoryFromStorage
) : TracksHistoryRepository {
    override fun getTrackHistoryList(): List<Track> {
        return tracksHistoryStorage.getTracks()

    }
}


