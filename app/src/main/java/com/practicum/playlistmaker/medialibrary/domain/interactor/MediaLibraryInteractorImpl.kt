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

    override fun getAllTracksFromPlaylist(playlistName: String): Flow<List<Track>> {
        return mediaLibraryRepository.getAllTracksFromPlaylist(playlistName)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return mediaLibraryRepository.getPlaylists()
    }

    override fun getPlaylistByName(playListName: String): Flow<Playlist>{
        return mediaLibraryRepository.getPlaylistByName(playListName)
    }

//    override suspend fun getPlaylistByName(playlistName: String): Playlist {
//        return mediaLibraryRepository.getPlaylistByName(playlistName)
//    }

    override suspend fun deleteTrackFromPlaylist(playlistName: String, trackId: String) {
        mediaLibraryRepository.deleteTrackFromPlaylist(playlistName, trackId)
    }

    override suspend fun updatePlaylist(
        oldPlaylistName: String, newPlaylistName: String,
        newDescription: String, newCoverUri: String,
    ) {
        mediaLibraryRepository.updatePlaylist(
            oldPlaylistName, newPlaylistName,
            newDescription, newCoverUri
        )
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