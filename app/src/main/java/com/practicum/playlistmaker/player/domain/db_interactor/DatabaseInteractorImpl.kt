package com.practicum.playlistmaker.player.domain.db_interactor

import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.player.domain.repository.DatabaseRepository
import com.practicum.playlistmaker.player.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow

class DatabaseInteractorImpl(
    private val databaseRepository: DatabaseRepository
): DatabaseInteractor {

    override suspend fun saveTrackToDatabase(track: Track) {
        databaseRepository.saveTrackToDatabase(track)
    }

    override suspend fun removeFromFavorite(track: Track) {
        databaseRepository.removeFromFavorite(track)
    }

    override suspend fun updateIsFavoriteStatus(isFavorite: Boolean, track: Track) {
        databaseRepository.updateIsFavoriteStatus(isFavorite, track)
    }

    override suspend fun getAllTracksFromPlaylist(playlistId: Long): List<Track> {
        return databaseRepository.getAllTracksFromPlaylist(playlistId)
    }

    override suspend fun insertPlayListTrackCrossRef(playlistId: Long, track: Track): Long {
        return databaseRepository.insertPlayListTrackCrossRef(playlistId, track)
    }

    override suspend fun checkPlaylistHasTrack(trackId: String, playlistId: Long): Boolean {
        return databaseRepository.checkPlaylistHasTrack(trackId, playlistId)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return databaseRepository.getPlaylists()
    }

    override fun getPlaylistsWithTrackCount(): Flow<List<Playlist>> {
        return databaseRepository.getPlaylistsWithTrackCount()
    }


}