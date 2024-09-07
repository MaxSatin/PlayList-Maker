package com.practicum.playlistmaker.data.storage.manipulator

import com.practicum.playlistmaker.domain.model.Track

interface GetTracksHistoryFromStorage {
    fun getTracks(): List<Track>
}