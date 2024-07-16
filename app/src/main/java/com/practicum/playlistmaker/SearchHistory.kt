package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.databinding.ActivitySearchBinding

class SearchHistory(
    private val sharedPrefs: SharedPreferences,
    private val historyTracksAdapter: HistoryRVAdapter
) {

    companion object {
        const val KEY_HISTORY_TRACK_LIST = "history_track_list"
    }

    private val gson: Gson = Gson()
    private var historyTracks: List<CurrentTrack> = getTracks()


    fun addTracksToHistory(track: CurrentTrack) {
        val index = historyTracks.indexOfFirst { it.trackId == track.trackId }
        if (index >= 0) {
            val newHistoryTrackList = historyTracks.toMutableList()
            newHistoryTrackList.removeAt(index)
            newHistoryTrackList.add(0, track)
            updateTracks(newHistoryTrackList)
        } else if (historyTracks.size > 9) {
            val newHistoryTrackList = historyTracks.toMutableList()
            newHistoryTrackList.removeAt(9)
            newHistoryTrackList.add(0, track)
            updateTracks(newHistoryTrackList)
        } else {
            val newHistoryTrackList = historyTracks.toMutableList()
            newHistoryTrackList.add(0, track)
            updateTracks(newHistoryTrackList)
        }

        saveTrackToLocalStorage(historyTracks)
    }

    fun updateTracks(newTracks: List<CurrentTrack>) {
        historyTracks = newTracks
        historyTracksAdapter.updateItems(historyTracks)
        saveTrackToLocalStorage(historyTracks)
    }

    fun saveTrackToLocalStorage(tracks: List<CurrentTrack>) {
        val trackToGson: String = gson.toJson(tracks)
        sharedPrefs.edit()
            .putString(KEY_HISTORY_TRACK_LIST, trackToGson)
            .apply()
    }

    fun getTrackFromLocalStorage(): List<CurrentTrack>? {
        val tracksFromGson: String? = sharedPrefs.getString(KEY_HISTORY_TRACK_LIST, null)
        return tracksFromGson?.let {
            val itemType = object : TypeToken<List<CurrentTrack>>() {}.type
            gson.fromJson(tracksFromGson, itemType)
        }
    }

    fun getTracks(): List<CurrentTrack> {
        val tracksFromStorage = getTrackFromLocalStorage()
        return if (!tracksFromStorage.isNullOrEmpty()) {
            tracksFromStorage
        } else {
            val emptyTrackList = emptyList<CurrentTrack>()
            emptyTrackList
        }
    }

    fun isTrackHistoryEmpty(): Boolean {
        return historyTracks.isNullOrEmpty()
    }
}


