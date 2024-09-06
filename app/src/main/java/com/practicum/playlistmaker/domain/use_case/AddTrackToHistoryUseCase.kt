package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.CurrentTrack
import com.practicum.playlistmaker.data.storage.manipulator.GetTracksHistoryFromStorageManip
import com.practicum.playlistmaker.data.storage.manipulator.SaveTrackHistoryToStorageManip
import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.consumer.ConsumerData
import com.practicum.playlistmaker.domain.interactors.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.domain.model.Track

class AddTrackToHistoryUseCase(
    private val saveTrackHistory: SaveTrackHistoryToStorageManip,
    private val getTracksHistory: GetTracksHistoryFromStorageManip
) : AddTrackToHistoryInteractor {

    override fun addTracksToHistory(track: Track) {
        val currentHistoryList = getTracksHistory.getTracks()
        val index = currentHistoryList.indexOfFirst { it.trackId == track.trackId }
        if(index >= 0) {
            val newCurrentHistoryList = currentHistoryList.toMutableList()
            newCurrentHistoryList.removeAt(index)
            newCurrentHistoryList.add(0, track)

        }

    }

    fun updateTracks(newTracks: List<Track>) {
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