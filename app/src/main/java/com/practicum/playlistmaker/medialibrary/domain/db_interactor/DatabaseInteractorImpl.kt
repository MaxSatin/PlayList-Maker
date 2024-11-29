package com.practicum.playlistmaker.medialibrary.domain.db_interactor

import com.practicum.playlistmaker.medialibrary.domain.repository.DatabaseRepository
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import kotlinx.coroutines.flow.Flow

class DatabaseInteractorImpl(
    private val databaseRepository: DatabaseRepository
): DatabaseInteractor {
    override fun getFavoriteTrackList(): Flow<List<Track>> {
       return databaseRepository.getFavoriteTrackList()
    }
}