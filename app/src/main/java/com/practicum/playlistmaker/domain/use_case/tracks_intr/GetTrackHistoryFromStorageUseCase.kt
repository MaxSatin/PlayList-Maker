package com.practicum.playlistmaker.domain.use_case.tracks_intr

import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.domain.model.Track

class GetTrackHistoryFromStorageUseCase(
    private val tracksHistoryRepository: TracksHistoryRepository
) {
    operator fun invoke(): List<Track> {
        return tracksHistoryRepository.getTracks()
    }
}


