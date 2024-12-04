package com.practicum.playlistmaker.search.domain.database_interactor

import com.practicum.playlistmaker.search.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow

class DatabaseInteractorImpl(
    private val databaseRepository: DatabaseRepository
): DatabaseInteractor {
    override fun getFavoriteTracksId(): Flow<List<String>> {
        return databaseRepository.getFavoriteTracksId()
    }
}