package com.practicum.playlistmaker.domain.use_case.tracks_intr

import com.practicum.playlistmaker.data.storage.manipulator.SaveTrackHistoryToStorage
import com.practicum.playlistmaker.domain.interactors.AddTrackToHistoryIntr
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository

class AddTrackToHistoryUseCase(
    private val saveTrackHistory: SaveTrackHistoryToStorage,
    private val tracksHistoryRepository: TracksHistoryRepository
) : AddTrackToHistoryIntr {

    override fun addTracksToHistory(track: Track) {
        val currentHistoryList = tracksHistoryRepository.getTracks()
        val index = currentHistoryList.indexOfFirst { it.trackId == track.trackId }
        if (index >= 0) {
            val newCurrentHistoryList = currentHistoryList.toMutableList()
            newCurrentHistoryList.removeAt(index)
            newCurrentHistoryList.add(0, track)
            updateTracks(newCurrentHistoryList)
        } else if (currentHistoryList.size > 9) {
            val newCurrentHistoryList = currentHistoryList.toMutableList()
            newCurrentHistoryList.removeAt(9)
            newCurrentHistoryList.add(0, track)
            updateTracks(newCurrentHistoryList)
        } else {
            val newCurrentHistoryList = currentHistoryList.toMutableList()
            newCurrentHistoryList.add(0, track)
            updateTracks(newCurrentHistoryList)
        }
    }

    private fun updateTracks(newTracks: List<Track>) {
        saveTrackHistory.saveTracksHistoryToLocalStorage(newTracks)
    }
}

//fun addTracksToHistory(track: CurrentTrack) {
//    val index = historyTracks.indexOfFirst { it.trackId == track.trackId }
//    if (index >= 0) {
//        val newHistoryTrackList = historyTracks.toMutableList()
//        newHistoryTrackList.removeAt(index)
//        newHistoryTrackList.add(0, track)
//        updateTracks(newHistoryTrackList)
//    } else if (historyTracks.size > 9) {
//        val newHistoryTrackList = historyTracks.toMutableList()
//        newHistoryTrackList.removeAt(9)
//        newHistoryTrackList.add(0, track)
//        updateTracks(newHistoryTrackList)
//    } else {
//        val newHistoryTrackList = historyTracks.toMutableList()
//        newHistoryTrackList.add(0, track)
//        updateTracks(newHistoryTrackList)
//    }
//
//    saveTrackToLocalStorage(historyTracks)
//}