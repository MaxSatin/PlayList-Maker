package com.practicum.playlistmaker.medialibrary.domain.interactor

import com.practicum.playlistmaker.medialibrary.domain.repository.MediaLibraryRepository
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import kotlinx.coroutines.flow.Flow

class MediaLibraryInteractorImpl(
    private val mediaLibraryRepository: MediaLibraryRepository,
) : MediaLibraryInteractor {

    override fun getFavoriteTrackList(): Flow<List<Track>> {
        return mediaLibraryRepository.getFavoriteTrackList()

//    override fun getFavoriteTrackList(): List<Track> {
//        return mediaLibraryRepository.getFavoriteTrackList()
//    }
//
    }

    override fun getPlaylists(): List<List<Track>> {
        return mediaLibraryRepository.getPlaylists()
    }

}