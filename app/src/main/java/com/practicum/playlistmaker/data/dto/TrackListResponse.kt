package com.practicum.playlistmaker.data.dto

class TrackListResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
): NetworkResponse()
