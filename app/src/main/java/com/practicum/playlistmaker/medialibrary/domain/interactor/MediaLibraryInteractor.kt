package com.practicum.playlistmaker.medialibrary.domain.interactor

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow

interface MediaLibraryInteractor {
    fun getAllTracksFromPlaylist(playlistName: String): Flow<List<Track>>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylistByName(playlistName: String): Playlist

    suspend fun deleteTrackFromPlaylist(playlistName: String, trackId: String)

    suspend fun updatePlaylist(oldPlaylistName: String, newPlaylistName: String)

    suspend fun addPlaylistWithReplace(playlist: Playlist)

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getFavoriteTrackList(): Flow<List<Track>>
}