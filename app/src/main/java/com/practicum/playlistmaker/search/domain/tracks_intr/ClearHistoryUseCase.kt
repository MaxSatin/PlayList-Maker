package com.practicum.playlistmaker.search.domain.tracks_intr

import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository

class ClearHistoryUseCase(
    private val tracksHistoryRepository: TracksHistoryRepository
) {
    operator fun invoke() {
        tracksHistoryRepository.clearStorage()
    }
}