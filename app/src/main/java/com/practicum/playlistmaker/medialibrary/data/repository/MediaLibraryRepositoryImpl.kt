package com.practicum.playlistmaker.medialibrary.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.data.utils.TrackDbConverter
import com.practicum.playlistmaker.medialibrary.domain.repository.MediaLibraryRepository
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class MediaLibraryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TrackDbConverter,
    private val gson: Gson,
    private val sharedPrefs: SharedPreferences

):MediaLibraryRepository {

    override fun getFavoriteTrackList(): Flow<List<Track>> = flow {
        val favoriteTrackListFlow = appDatabase.mediaLibraryTrackDao().getFavoriteTrackList()
        favoriteTrackListFlow.collect { favoriteTrackList ->
            val reversedTrackList = favoriteTrackList.reversed()
            emit(convertFromTrackEntity(reversedTrackList))
        }
    }


    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { trackEntity ->
            converter.map(trackEntity)
        }
    }
    //
//    override fun getFavoriteTrackList(): List<Track> {
//        val favoriteTrackList = getFavoriteTrackListFromStorage()
//        return if (favoriteTrackList.isNullOrEmpty()){
//            emptyList<Track>()
//        } else {
//            favoriteTrackList
//        }
//    }
//
    override fun getPlaylists(): List<List<Track>> {
        val playlists = getPlaylistsFromStorage()
        return if (playlists.isNullOrEmpty()){
            emptyList<List<Track>>()
        } else {
            playlists
        }
    }
//
//    private fun getFavoriteTrackListFromStorage(): List<Track>? {
//        val favoriteTrackListGson: String? = sharedPrefs.getString(FAVORITE_TRACK_LIST_KEY, null)
//        return favoriteTrackListGson?.let{
//            val itemType = object : TypeToken<List<Track>> () {}.type
//            gson.fromJson(it, itemType)
//        }
//    }
//
    private fun getPlaylistsFromStorage(): List<List<Track>>? {
        val playlistsGson: String? = sharedPrefs.getString(PLAYLIST_KEY, null)
        return playlistsGson?.let {
            val itemType = object: TypeToken<List<List<Track>>>(){}.type
            gson.fromJson(it, itemType)
        }
    }

    companion object {
        private const val FAVORITE_TRACK_LIST_KEY = "favorite_track_list_key"
        private const val FAVORITE_SHARED_PREFS_KEY = "favorite_track_list_shared_prefs"
        private const val PLAYLIST_KEY = "playlist_key"
        private const val PLAYLIST_SHARED_PREFS_KEY = "playlist_shared_prefs"
    }
}