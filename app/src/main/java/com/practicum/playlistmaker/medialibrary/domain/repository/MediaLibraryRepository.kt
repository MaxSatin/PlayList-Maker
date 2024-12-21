package com.practicum.playlistmaker.medialibrary.domain.repository

import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow

interface MediaLibraryRepository {
    //    fun getFavoriteTrackList(): List<Track>
    suspend fun getAllTracksFromPlaylist(playlistName: String): List<Track>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylistWithReplace(playlist: Playlist)

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getFavoriteTrackList(): Flow<List<Track>>
}