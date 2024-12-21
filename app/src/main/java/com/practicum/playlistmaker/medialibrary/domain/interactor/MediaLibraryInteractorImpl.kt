package com.practicum.playlistmaker.medialibrary.domain.interactor

import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.repository.MediaLibraryRepository
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow

class MediaLibraryInteractorImpl(
    private val mediaLibraryRepository: MediaLibraryRepository,
) : MediaLibraryInteractor {

    override fun getFavoriteTrackList(): Flow<List<Track>> {
        return mediaLibraryRepository.getFavoriteTrackList()

    }

    override suspend fun getAllTracksFromPlaylist(playlistName: String): List<Track> {
        return mediaLibraryRepository.getAllTracksFromPlaylist(playlistName)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return mediaLibraryRepository.getPlaylists()
    }

    override suspend fun addPlaylistWithReplace(playlist: Playlist) {
        mediaLibraryRepository.addPlaylistWithReplace(playlist)
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        mediaLibraryRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        mediaLibraryRepository.deletePlaylist(playlist)
    }

}