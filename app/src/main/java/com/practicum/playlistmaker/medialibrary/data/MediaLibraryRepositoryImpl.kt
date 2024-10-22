package com.practicum.playlistmaker.medialibrary.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.medialibrary.domain.repository.MediaLibraryRepository
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track


class MediaLibraryRepositoryImpl(
    private val gson: Gson,
    private val sharedPrefs: SharedPreferences

):MediaLibraryRepository {

    override fun getFavoriteTrackList(): List<Track> {
        val favoriteTrackList = getFavoriteTrackListFromStorage()
        return if (favoriteTrackList.isNullOrEmpty()){
            emptyList<Track>()
        } else {
            favoriteTrackList
        }
    }

    override fun getPlaylists(): List<List<Track>> {
        val playlists = getPlaylistsFromStorage()
        return if (playlists.isNullOrEmpty()){
            emptyList<List<Track>>()
        } else {
            playlists
        }
    }

    private fun getFavoriteTrackListFromStorage(): List<Track>? {
        val favoriteTrackListGson: String? = sharedPrefs.getString(FAVORITE_TRACK_LIST_KEY, null)
        return favoriteTrackListGson?.let{
            val itemType = object : TypeToken<List<Track>> () {}.type
            gson.fromJson(it, itemType)
        }
    }

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