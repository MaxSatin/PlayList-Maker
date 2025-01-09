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

    override fun getAllTracksFromPlaylist(playlistId: Long): Flow<List<Track>> {
        return mediaLibraryRepository.getAllTracksFromPlaylist(playlistId)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return mediaLibraryRepository.getPlaylists()
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist>{
        return mediaLibraryRepository.getPlaylistById(playlistId)
    }

//    override suspend fun getPlaylistByName(playlistName: String): Playlist {
//        return mediaLibraryRepository.getPlaylistByName(playlistName)
//    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: String) {
        mediaLibraryRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun updatePlaylistTable(
        playlistId: Long, newPlaylistName: String,
        newDescription: String, newCoverUri: String,
    ) {
        mediaLibraryRepository.updatePlaylistTable(
            playlistId, newPlaylistName,
            newDescription, newCoverUri
        )
    }

    override suspend fun addPlaylistWithReplace(playlist: Playlist) {
        mediaLibraryRepository.addPlaylistWithReplace(playlist)
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        mediaLibraryRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playListId: Long) {
        mediaLibraryRepository.deletePlaylist(playListId)
    }

//    override suspend fun deletePlaylist(playlist: Playlist) {
//        mediaLibraryRepository.deletePlaylist(playlist)
//    }

}