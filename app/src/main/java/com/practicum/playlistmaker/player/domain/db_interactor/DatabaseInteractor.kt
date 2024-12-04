package com.practicum.playlistmaker.player.domain.db_interactor

import com.practicum.playlistmaker.player.presentation.model.Track

interface DatabaseInteractor {
    suspend fun saveTrackToDatabase(track: Track)
    suspend fun removeFromFavorite(track: Track)
}