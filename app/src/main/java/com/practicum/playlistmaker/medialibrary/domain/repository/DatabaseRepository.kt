package com.practicum.playlistmaker.medialibrary.domain.repository

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    fun getFavoriteTrackList(): Flow<List<Track>>

}