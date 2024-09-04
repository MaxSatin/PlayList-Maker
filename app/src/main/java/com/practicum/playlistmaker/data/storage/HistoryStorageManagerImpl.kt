package com.practicum.playlistmaker.data.storage

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.domain.model.Track

private const val KEY_HISTORY_TRACK_LIST = "history_track_list"
private const val SHAREDPREFS_TRACKS_HISTORY = "history_track_list_shared_prefs"

class HistoryStorageManagerImpl(
    private val sharedPrefs: SharedPrefsClient
) : TracksHistoryStorageManager<Track> {

    private val gson = Creator.getGson()
    private val sharedPrefsHistory = sharedPrefs.getSharedPrefsHistoryTrackList()

    override fun saveTracksHistoryToLocalStorage(data: List<Track>) {
        val trackToGson: String = gson.toJson(data)
        sharedPrefsHistory.edit()
            .putString(KEY_HISTORY_TRACK_LIST, trackToGson)
            .apply()
    }

    override fun getTracksHistoryFromLocalStorage(): List<Track>? {
        val tracksFromGson: String? = sharedPrefsHistory.getString(KEY_HISTORY_TRACK_LIST, null)
        return tracksFromGson?.let {
            val itemType = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(tracksFromGson, itemType)
        }
    }
}
