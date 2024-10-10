package com.practicum.playlistmaker.search.data.dto

class TrackListResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
): NetworkResponse()
