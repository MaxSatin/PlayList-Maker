package com.practicum.playlistmaker.domain.interactors

import com.practicum.playlistmaker.domain.model.Track

interface GetTrackHistoryIntr {
    fun getTracks(): List<Track>
    fun isHistoryEmpty(): Boolean
}