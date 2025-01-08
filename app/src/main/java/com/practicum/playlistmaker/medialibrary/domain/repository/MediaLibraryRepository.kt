package com.practicum.playlistmaker.medialibrary.domain.repository

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow

interface MediaLibraryRepository {
    fun getAllTracksFromPlaylist(playlistId: Long): Flow<List<Track>>

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylistById(playlistId: Long): Flow<Playlist>

    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: String)

    suspend fun updatePlaylistTable(playListId: Long, newPlaylistName: String, newDescription: String, newCoverUri: String)

    suspend fun addPlaylistWithReplace(playlist: Playlist)

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getFavoriteTrackList(): Flow<List<Track>>

    //    suspend fun getPlaylistByName(playlistName: String): Playlist
}