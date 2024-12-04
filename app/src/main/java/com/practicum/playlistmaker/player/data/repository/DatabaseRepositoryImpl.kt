package com.practicum.playlistmaker.player.data.repository

import com.practicum.playlistmaker.AppDatabase
import com.practicum.playlistmaker.player.data.db.entity.TrackEntity
import com.practicum.playlistmaker.player.data.utils.TrackDbConverter
import com.practicum.playlistmaker.player.domain.repository.DatabaseRepository
import com.practicum.playlistmaker.player.presentation.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TrackDbConverter
): DatabaseRepository {

    override suspend fun saveTrackToDatabase(track: Track) {
        withContext(Dispatchers.IO) {
            val trackEntity = converter.map(track)
            appDatabase.playerTrackDao().insertTrack(trackEntity)
        }
    }

    override suspend fun removeFromFavorite(track: Track) {
        withContext(Dispatchers.IO) {
            val trackEntity = converter.map(track)
            appDatabase.playerTrackDao().removeFromFavorite(trackEntity)
        }
    }
}