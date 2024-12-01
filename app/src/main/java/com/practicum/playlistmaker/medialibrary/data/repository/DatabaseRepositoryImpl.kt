package com.practicum.playlistmaker.medialibrary.data.repository

import com.practicum.playlistmaker.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.utils.TrackDbConverter
import com.practicum.playlistmaker.medialibrary.domain.repository.DatabaseRepository
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TrackDbConverter,
) : DatabaseRepository {

    override fun getFavoriteTrackList(): Flow<List<Track>> = flow {
        val favoriteTrackListFlow = appDatabase.mediaLibraryTrackDao().getFavoriteTrackList()
        favoriteTrackListFlow.collect { favoriteTrackList ->
            emit(convertFromTrackEntity(favoriteTrackList))
        }
    }


    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { trackEntity ->
            converter.map(trackEntity)
        }
    }
}