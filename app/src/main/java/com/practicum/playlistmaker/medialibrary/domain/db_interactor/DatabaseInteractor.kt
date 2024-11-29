package com.practicum.playlistmaker.medialibrary.domain.db_interactor

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseInteractor {

    fun getFavoriteTrackList(): Flow<List<Track>>

}