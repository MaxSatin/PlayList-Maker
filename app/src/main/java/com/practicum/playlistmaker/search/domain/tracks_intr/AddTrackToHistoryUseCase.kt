package com.practicum.playlistmaker.search.domain.tracks_intr

import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository

class AddTrackToHistoryUseCase(
    private val tracksHistoryRepository: TracksHistoryRepository
) {

    operator fun invoke(track: Track) {
        val currentHistoryList = tracksHistoryRepository.getTracks()
        val index = currentHistoryList.indexOfFirst { it.trackId == track.trackId }
        if (index >= 0) {
            val newCurrentHistoryList = currentHistoryList.toMutableList()
            newCurrentHistoryList.removeAt(index)
            newCurrentHistoryList.add(0, track)
            tracksHistoryRepository.saveTracksHistoryToLocalStorage(newCurrentHistoryList)
        } else if (currentHistoryList.size > 9) {
            val newCurrentHistoryList = currentHistoryList.toMutableList()
            newCurrentHistoryList.removeAt(9)
            newCurrentHistoryList.add(0, track)
            tracksHistoryRepository.saveTracksHistoryToLocalStorage(newCurrentHistoryList)
        } else {
            val newCurrentHistoryList = currentHistoryList.toMutableList()
            newCurrentHistoryList.add(0, track)
            tracksHistoryRepository.saveTracksHistoryToLocalStorage(newCurrentHistoryList)
        }
    }


}
