package com.practicum.playlistmaker.medialibrary.domain.interactor

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import kotlinx.coroutines.flow.Flow

interface MediaLibraryInteractor {
    fun getFavoriteTrackList(): Flow<List<Track>>
//    fun getFavoriteTrackList(): List<Track>
    fun getPlaylists(): List<List<Track>>
}