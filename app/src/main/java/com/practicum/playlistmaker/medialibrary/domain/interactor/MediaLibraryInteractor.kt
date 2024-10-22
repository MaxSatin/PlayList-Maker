package com.practicum.playlistmaker.medialibrary.domain.interactor

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track

interface MediaLibraryInteractor {
    fun getFavoriteTrackList(): List<Track>
    fun getPlaylists(): List<List<Track>>
}