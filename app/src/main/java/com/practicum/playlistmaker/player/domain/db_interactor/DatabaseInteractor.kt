package com.practicum.playlistmaker.player.domain.db_interactor


import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.player.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseInteractor {
    suspend fun saveTrackToDatabase(track: Track)
    suspend fun removeFromFavorite(track: Track)
    suspend fun getAllTracksFromPlaylist(playlistName: String): List<Track>
    suspend fun insertPlayListTrackCrossRef(playlistName: String, trackId: String)
    fun getPlaylists(): Flow<List<Playlist>>
}