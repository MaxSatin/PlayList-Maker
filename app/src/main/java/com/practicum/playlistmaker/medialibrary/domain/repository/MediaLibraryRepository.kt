package com.practicum.playlistmaker.medialibrary.domain.repository

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import kotlinx.coroutines.flow.Flow

interface MediaLibraryRepository {
    //    fun getFavoriteTrackList(): List<Track>
    fun getPlaylists(): List<List<Track>>
    fun getFavoriteTrackList(): Flow<List<Track>>
}