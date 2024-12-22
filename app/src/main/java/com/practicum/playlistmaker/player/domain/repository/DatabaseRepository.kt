package com.practicum.playlistmaker.player.domain.repository

import com.practicum.playlistmaker.player.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.player.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun saveTrackToDatabase(track: Track)
    suspend fun removeFromFavorite(track: Track)
    suspend fun getAllTracksFromPlaylist(playlistName: String): List<Track>
    suspend fun insertPlayListTrackCrossRef(playlistName: String, track: Track)
    suspend fun checkPlaylistHasTrack(trackId: String, playlistName: String): Boolean
    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistsWithTrackCount(): Flow<List<Playlist>>
}