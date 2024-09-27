package com.practicum.playlistmaker.search.data.dto

import okhttp3.Request

open class NetworkResponse {
    var resultCode = 0
    var requestInfo: Request? = null
}