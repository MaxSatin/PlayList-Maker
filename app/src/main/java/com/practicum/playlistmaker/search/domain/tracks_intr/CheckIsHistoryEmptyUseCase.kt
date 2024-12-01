package com.practicum.playlistmaker.search.domain.tracks_intr

import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository

class CheckIsHistoryEmptyUseCase(
    private val tracksHistoryRepository: TracksHistoryRepository
) {
//    operator fun invoke(): Boolean {
//        return tracksHistoryRepository.isHistoryEmpty()
//    }
}