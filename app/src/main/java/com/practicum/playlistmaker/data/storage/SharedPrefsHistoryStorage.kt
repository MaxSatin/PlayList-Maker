package com.practicum.playlistmaker.data.storage

import android.content.Context
import com.practicum.playlistmaker.CurrentTrack
import com.practicum.playlistmaker.domain.model.Track

private const val KEY_HISTORY_TRACK_LIST = "history_track_list"

class SharedPrefsHistoryStorage(private val sharedPrefs: SharedPrefsClient) : TracksHistoryStorage {

    override fun saveTracksHistoryToLocalStorage(tracks: List<Track>) {

    }

    override fun getTracksHistoryFromLocalStorage(): List<CurrentTrack>? {

    }
}