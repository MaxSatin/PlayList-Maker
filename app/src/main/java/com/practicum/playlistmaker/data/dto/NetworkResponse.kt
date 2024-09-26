package com.practicum.playlistmaker.data.dto

import okhttp3.Request

open class NetworkResponse {
    var resultCode = 0
    var requestInfo: Request? = null
}