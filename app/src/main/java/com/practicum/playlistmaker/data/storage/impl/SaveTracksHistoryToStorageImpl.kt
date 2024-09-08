package com.practicum.playlistmaker.data.storage.impl

import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.Creator.GsonProvider
import com.practicum.playlistmaker.data.Constants
import com.practicum.playlistmaker.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.data.storage.manipulator.SaveTrackHistoryToStorage
import com.practicum.playlistmaker.domain.model.Track

class SaveTracksHistoryToStorageImpl(
    private val sharedPrefs: SharedPrefsClient
) : SaveTrackHistoryToStorage{

    private val gson = GsonProvider.gson
    private val sharedPrefsHistory = sharedPrefs.getSharedPrefs()

    override fun saveTracksHistoryToLocalStorage(data: List<Track>) {
        val trackToGson: String = gson.toJson(data)
        sharedPrefsHistory.edit()
            .putString(Constants.KEY_HISTORY_TRACK_LIST, trackToGson)
            .apply()
    }
}
