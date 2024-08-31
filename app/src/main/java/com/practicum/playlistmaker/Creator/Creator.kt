package com.practicum.playlistmaker.Creator

import com.google.gson.Gson

object Creator {


    fun getGson(): Gson {
        return Gson()
    }
}