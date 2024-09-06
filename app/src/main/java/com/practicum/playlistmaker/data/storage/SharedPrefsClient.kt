package com.practicum.playlistmaker.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.data.Constants


class SharedPrefsClient(context: Context) {

    private val sharedPrefs = context.getSharedPreferences(Constants.SHAREDPREFS_TRACKS_HISTORY, Context.MODE_PRIVATE)

    fun getSharedPrefsHistoryTrackList(): SharedPreferences {
        return sharedPrefs
    }
}