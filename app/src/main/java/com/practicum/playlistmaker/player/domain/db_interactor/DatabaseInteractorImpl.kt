package com.practicum.playlistmaker.player.domain.db_interactor

import com.practicum.playlistmaker.player.domain.repository.DatabaseRepository
import com.practicum.playlistmaker.player.presentation.model.Track

class DatabaseInteractorImpl(
    private val databaseRepository: DatabaseRepository
): DatabaseInteractor {

    override suspend fun saveTrackToDatabase(track: Track) {
        databaseRepository.saveTrackToDatabase(track)
    }
}