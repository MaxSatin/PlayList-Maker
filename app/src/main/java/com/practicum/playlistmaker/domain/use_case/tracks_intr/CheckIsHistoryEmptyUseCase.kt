package com.practicum.playlistmaker.domain.use_case.tracks_intr

import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository

class CheckIsHistoryEmptyUseCase(
    private val tracksHistoryRepository: TracksHistoryRepository
) {
    operator fun invoke(): Boolean {
        return tracksHistoryRepository.isHistoryEmpty()
    }
}