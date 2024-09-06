package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.CurrentTrack
import com.practicum.playlistmaker.data.storage.manipulator.GetTracksHistoryFromStorageManip
import com.practicum.playlistmaker.domain.interactors.GetTracksHistoryInteractor
import com.practicum.playlistmaker.domain.model.Track

class GetTrackHistoryFromStorageUseCase(
    private val tracksHistoryStorageManip: GetTracksHistoryFromStorageManip
) : GetTracksHistoryInteractor {
    override fun getTrackHistoryList(): List<Track> {
        val tracksFromStorage = tracksHistoryStorageManip.getTracks()
        return tracksFromStorage
    }
}


