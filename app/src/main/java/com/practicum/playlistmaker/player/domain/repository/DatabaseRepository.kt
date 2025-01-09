package com.practicum.playlistmaker.player.domain.repository

import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.player.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun saveTrackToDatabase(track: Track)
    suspend fun removeFromFavorite(track: Track)
    suspend fun updateIsFavoriteStatus(isFavorite: Boolean, track: Track)
    suspend fun getAllTracksFromPlaylist(playlistId: Long): List<Track>
    suspend fun insertPlayListTrackCrossRef(playlistId: Long, track: Track): Long
    suspend fun checkPlaylistHasTrack(trackId: String, playlistId: Long): Boolean
    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistsWithTrackCount(): Flow<List<Playlist>>
}