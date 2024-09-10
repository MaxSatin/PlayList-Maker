package com.practicum.playlistmaker.data.storage.impl

import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.Creator.GsonProvider
import com.practicum.playlistmaker.data.Constants
import com.practicum.playlistmaker.data.storage.SharedPrefsClient

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository

class TracksHistoryRepositoryImpl(
    private val sharedPrefs: SharedPrefsClient
) : TracksHistoryRepository {

    private val gson = GsonProvider.gson
    private val sharedPrefsHistory = sharedPrefs.getSharedPrefs()

    override fun saveTracksHistoryToLocalStorage(data: List<Track>) {
        val trackToGson: String = gson.toJson(data)
        sharedPrefsHistory.edit()
            .putString(Constants.KEY_HISTORY_TRACK_LIST, trackToGson)
            .apply()
    }

    override fun clearStorage() {
        sharedPrefsHistory.edit()
            .putString(Constants.KEY_HISTORY_TRACK_LIST, "")
            .apply()
    }

    override fun getTracks(): List<Track> {
        val tracksFromStorage = getTrackFromLocalStorage()
        return if (!tracksFromStorage.isNullOrEmpty()) {
            tracksFromStorage
        } else {
            val emptyTrackList = emptyList<Track>()
            emptyTrackList
        }
    }

    override fun isHistoryEmpty(): Boolean {
        return getTracks().isNullOrEmpty()
    }

    private fun getTrackFromLocalStorage(): List<Track>? {
        val tracksFromGson: String? = sharedPrefsHistory.getString(Constants.KEY_HISTORY_TRACK_LIST, null)
        return tracksFromGson?.let {
            val itemType = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(tracksFromGson, itemType)
        }
    }
}