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

    override suspend fun getAllTracksFromPlaylist(playlistName: String): List<Track> {
        return databaseRepository.getAllTracksFromPlaylist(playlistName)
    }

    override suspend fun insertPlayListTrackCrossRef(playlistName: String, track: Track): Long {
        return databaseRepository.insertPlayListTrackCrossRef(playlistName, track)
    }

    override suspend fun checkPlaylistHasTrack(trackId: String, playlistName: String): Boolean {
        return databaseRepository.checkPlaylistHasTrack(trackId, playlistName)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return databaseRepository.getPlaylists()
    }

    override fun getPlaylistsWithTrackCount(): Flow<List<Playlist>> {
        return databaseRepository.getPlaylistsWithTrackCount()
    }


}