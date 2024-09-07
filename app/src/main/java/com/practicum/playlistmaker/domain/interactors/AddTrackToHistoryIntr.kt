package com.practicum.playlistmaker.domain.interactors

import com.practicum.playlistmaker.domain.model.Track

interface AddTrackToHistoryIntr {
    fun addTracksToHistory(track: Track)
}