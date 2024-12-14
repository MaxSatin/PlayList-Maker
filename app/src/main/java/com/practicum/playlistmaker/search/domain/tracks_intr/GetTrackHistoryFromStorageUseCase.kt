package com.practicum.playlistmaker.search.domain.tracks_intr

import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.search.domain.track_model.Track
import kotlinx.coroutines.flow.Flow

class GetTrackHistoryFromStorageUseCase(
    private val tracksHistoryRepository: TracksHistoryRepository
) {
    operator fun invoke(): List<Track> {
        return tracksHistoryRepository.getTracks()
    }
}


