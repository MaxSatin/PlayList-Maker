package com.practicum.playlistmaker.player.domain.repository

import com.practicum.playlistmaker.player.data.db.entity.TrackEntity
import com.practicum.playlistmaker.player.presentation.model.Track

interface DatabaseRepository {
    suspend fun saveTrackToDatabase(track: Track)
    suspend fun removeFromFavorite(track: Track)
}