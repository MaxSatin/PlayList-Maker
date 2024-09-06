package com.practicum.playlistmaker.data.storage.manipulator

import com.practicum.playlistmaker.domain.model.Track

interface GetTracksHistoryFromStorageManip {
    fun getTracks(): List<Track>
}