package com.practicum.playlistmaker.Creator

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.data.storage.SharedPrefsClient

object Creator {

    fun getGson(): Gson {
        return Gson()
    }

}