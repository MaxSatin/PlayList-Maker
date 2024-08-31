package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.CurrentTrack

class TrackListResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
): NetworkResponse()
