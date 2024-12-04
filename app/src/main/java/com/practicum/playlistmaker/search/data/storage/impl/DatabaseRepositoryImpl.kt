package com.practicum.playlistmaker.search.data.storage.impl

import com.practicum.playlistmaker.AppDatabase
import com.practicum.playlistmaker.search.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow


class DatabaseRepositoryImpl(
    private val appDatabase: AppDatabase
): DatabaseRepository {
    override fun getFavoriteTracksId(): Flow<List<String>> {
        return appDatabase.searchTrackDao().getFavoriteTracksId()
    }
}