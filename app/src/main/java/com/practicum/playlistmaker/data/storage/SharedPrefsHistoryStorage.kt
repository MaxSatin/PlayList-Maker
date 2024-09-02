package com.practicum.playlistmaker.data.storage

import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.CurrentTrack
import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.domain.model.Track

private const val KEY_HISTORY_TRACK_LIST = "history_track_list"

class SharedPrefsHistoryStorage(
    private val sharedPrefs: SharedPrefsClient
) : TracksHistoryStorageManager {

    private val gson = Creator.getGson()
    private val sharedPrefsHistory = sharedPrefs.getSharedPrefsHistoryTrackList()

    override fun saveTracksHistoryToLocalStorage(tracks: List<Track>) {
        val trackToGson: String = gson.toJson(tracks)
        sharedPrefsHistory.edit()
            .putString(KEY_HISTORY_TRACK_LIST, trackToGson)
            .apply()
    }

    override fun getTracksHistoryFromLocalStorage(): List<CurrentTrack>? {
        val tracksFromGson: String? = sharedPrefsHistory.getString(KEY_HISTORY_TRACK_LIST, null)
        return tracksFromGson?.let {
            val itemType = object : TypeToken<List<CurrentTrack>>() {}.type
            gson.fromJson(tracksFromGson, itemType)
        }
    }
}
