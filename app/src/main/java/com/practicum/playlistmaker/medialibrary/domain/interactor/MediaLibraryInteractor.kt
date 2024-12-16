package com.practicum.playlistmaker.medialibrary.domain.interactor

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow

interface MediaLibraryInteractor {
//    fun getFavoriteTrackList(): Flow<List<Track>>
////    fun getFavoriteTrackList(): List<Track>
//    fun getPlaylists(): List<List<Track>>

    fun getAllTracksFromPlaylist(playlistName: String): Flow<List<Track>>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylistWithReplace(playlist: Playlist)

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getFavoriteTrackList(): Flow<List<Track>>
}