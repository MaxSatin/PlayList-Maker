package com.practicum.playlistmaker.search.data.storage.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.AppDatabase


import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksHistoryRepositoryImpl(
    private val gson: Gson,
    private val sharedPrefsHistory: SharedPreferences,
    private val appDatabase: AppDatabase
) : TracksHistoryRepository {

    override fun saveTracksHistoryToLocalStorage(data: List<Track>) {
        val trackToGson: String = gson.toJson(data)
        sharedPrefsHistory.edit()
            .putString(KEY_HISTORY_TRACK_LIST, trackToGson)
            .apply()
    }

    override fun clearStorage() {
        sharedPrefsHistory.edit()
            .putString(KEY_HISTORY_TRACK_LIST, "")
            .apply()
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val favoriteTracksIdsFlow = appDatabase.searchTrackDao().getFavoriteTracksId()
        favoriteTracksIdsFlow.collect { favoriteIds ->
            val tracksFromStorage = getTrackFromLocalStorage()
             if (!tracksFromStorage.isNullOrEmpty()) {
                 tracksFromStorage.map { track ->
                     track.isFavorite = favoriteIds.contains(track.trackId)
                 }
                emit(tracksFromStorage)
            } else {
                val emptyTrackList = emptyList<Track>()
                emit(emptyTrackList)
            }
        }
    }

    private fun getTrackFromLocalStorage(): List<Track>? {
        val tracksFromGson: String? = sharedPrefsHistory.getString(KEY_HISTORY_TRACK_LIST, null)
        return tracksFromGson?.let {
            val itemType = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(tracksFromGson, itemType)
        }
    }

    companion object {
        private const val KEY_HISTORY_TRACK_LIST = "history_track_list"
    }
}