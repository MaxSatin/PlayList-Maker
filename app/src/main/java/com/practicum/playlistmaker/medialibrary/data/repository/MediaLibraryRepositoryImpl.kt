package com.practicum.playlistmaker.medialibrary.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.data.utils.TrackDbConverter
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.repository.MediaLibraryRepository
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


class MediaLibraryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TrackDbConverter,
    private val gson: Gson,
    private val sharedPrefs: SharedPreferences

): MediaLibraryRepository {

    override fun getFavoriteTrackList(): Flow<List<Track>> = flow {
        val favoriteTrackListFlow = appDatabase.favoriteTracklistDao().getFavoriteTrackList()
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

    private fun convertFromPlaylistEntity(playlistEntitie: List<PlaylistEntity>): List<Playlist> {
        return playlistEntitie.map { playlistEntity ->
            converter.map(playlistEntity)
        }
    }

    override suspend fun getAllTracksFromPlaylist(playlistName: String): List<Track> {
        val trackList = appDatabase.playlistDao().getAllTracksFromPlaylist(playlistName)
            val reversedTracklist = trackList.reversed()
            return convertFromTrackEntity(reversedTracklist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playListFlow = appDatabase.playlistDao().getPlaylists()
        playListFlow.collect { playList ->
            val reversedPlaylist = playList.reversed()
            emit(convertFromPlaylistEntity(reversedPlaylist))
        }
    }

    override suspend fun addPlaylistWithReplace(playlist: Playlist) {
        withContext(Dispatchers.IO){
            val playlistEntity = converter.map(playlist)
            appDatabase.playlistDao().addPlaylistWithReplace(playlistEntity)
        }
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO){
            val playlistEntity = converter.map(playlist)
            appDatabase.playlistDao().addPlaylist(playlistEntity)
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO){
            val playlistEntity = converter.map(playlist)
            appDatabase.playlistDao().deletePlaylist(playlistEntity)
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
        private const val PLAYLIST_KEY = "playlist_key"

    }
}