package com.practicum.playlistmaker.domain.use_case.tracks_intr

import com.practicum.playlistmaker.domain.interactors.GetTrackHistoryIntr
import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.domain.model.Track

class GetTrackHistoryFromStorageUseCase(
    private val tracksHistoryRepository: TracksHistoryRepository
) : GetTrackHistoryIntr {
    override fun getTracks(): List<Track> {
        return tracksHistoryRepository.getTracks()
    }
}


