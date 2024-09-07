package com.practicum.playlistmaker.Creator

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.data.storage.SharedPrefsClient

object Creator {

    fun provideGson(): Gson {
        return Gson()
    }

    fun provideSharedPrefs(context: Context, key: String): SharedPrefsClient {
        return SharedPrefsClient(context, key)
    }

}