package com.practicum.playlistmaker.data.storage.impl

import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.data.Constants
import com.practicum.playlistmaker.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.data.storage.manipulator.GetTracksHistoryFromStorage
import com.practicum.playlistmaker.domain.model.Track

class GetTracksFromStorageImpl(
    private val sharedPrefs: SharedPrefsClient
) : GetTracksHistoryFromStorage {

    private val gson = Creator.provideGson()
    private val sharedPrefsHistory = sharedPrefs.getSharedPrefs()

    override fun getTracks(): List<Track> {
        val tracksFromStorage = getTrackFromLocalStorage()
        return if (!tracksFromStorage.isNullOrEmpty()) {
            tracksFromStorage
        } else {
            val emptyTrackList = emptyList<Track>()
            emptyTrackList
        }
    }
    private fun getTrackFromLocalStorage(): List<Track>? {
        val tracksFromGson: String? = sharedPrefsHistory.getString(Constants.KEY_HISTORY_TRACK_LIST, null)
        return tracksFromGson?.let {
            val itemType = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(tracksFromGson, itemType)
        }
    }
}