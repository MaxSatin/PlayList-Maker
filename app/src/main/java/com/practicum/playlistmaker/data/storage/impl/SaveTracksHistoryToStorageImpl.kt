package com.practicum.playlistmaker.data.storage.impl

import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.data.Constants
import com.practicum.playlistmaker.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.data.storage.manipulator.SaveTrackHistoryToStorageManip
import com.practicum.playlistmaker.domain.model.Track

class SaveTracksHistoryToStorageImpl(
    private val sharedPrefs: SharedPrefsClient
) : SaveTrackHistoryToStorageManip{

    private val gson = Creator.getGson()
    private val sharedPrefsHistory = sharedPrefs.getSharedPrefsHistoryTrackList()

    override fun saveTracksHistoryToLocalStorage(data: List<Track>) {
        val trackToGson: String = gson.toJson(data)
        sharedPrefsHistory.edit()
            .putString(Constants.KEY_HISTORY_TRACK_LIST, trackToGson)
            .apply()
    }
}
