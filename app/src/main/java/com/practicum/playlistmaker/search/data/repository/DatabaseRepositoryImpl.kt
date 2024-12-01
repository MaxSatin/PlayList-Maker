package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.AppDatabase
import com.practicum.playlistmaker.search.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow


//class DatabaseRepositoryImpl(
//    private val appDatabase: AppDatabase
//): DatabaseRepository {
//    override fun checkIsFavorite(trackId: String): Flow<Boolean>  {
//        return appDatabase.searchTrackDao().getFavoriteTracksId(trackId)
//    }
//}