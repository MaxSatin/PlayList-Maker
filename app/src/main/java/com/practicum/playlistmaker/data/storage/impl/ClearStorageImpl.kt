package com.practicum.playlistmaker.data.storage.impl

import androidx.core.content.edit
import com.practicum.playlistmaker.data.Constants
import com.practicum.playlistmaker.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.data.storage.manipulator.ClearLocalStorage

class ClearStorageImpl(
    private val sharedPrefsClient: SharedPrefsClient
) : ClearLocalStorage {
    override fun clearStorage() {
        val sharedPrefs = sharedPrefsClient.getSharedPrefs()
        sharedPrefs.edit()
            .putString(Constants.KEY_HISTORY_TRACK_LIST, "")
            .apply()
    }
}
