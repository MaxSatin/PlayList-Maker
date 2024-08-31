package com.practicum.playlistmaker.data.storage

import android.content.Context
import android.content.SharedPreferences


private const val SHAREDPREFS_TRACKS_HISTORY = "history_track_list"

class SharedPrefsClient(context: Context) {

    private val sharedPrefs = context.getSharedPreferences(SHAREDPREFS_TRACKS_HISTORY, Context.MODE_PRIVATE)

    fun getSharedPrefsHistoryTrackList(): SharedPreferences {
        return sharedPrefs
    }
}