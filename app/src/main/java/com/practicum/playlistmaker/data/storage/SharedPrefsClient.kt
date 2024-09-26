package com.practicum.playlistmaker.data.storage

import android.content.Context
import android.content.SharedPreferences


class SharedPrefsClient(private val context: Context, private val key: String) {

    fun getSharedPrefs(): SharedPreferences {
        val sharedPrefs = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        return sharedPrefs
    }
}