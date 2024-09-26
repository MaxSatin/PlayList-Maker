package com.practicum.playlistmaker.domain.use_case.tracks_intr

import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository

class ClearHistoryUseCase(
    private val tracksHistoryRepository: TracksHistoryRepository
) {
    operator fun invoke() {
        tracksHistoryRepository.clearStorage()
    }
}