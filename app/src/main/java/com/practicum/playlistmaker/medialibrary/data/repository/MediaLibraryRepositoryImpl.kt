package com.practicum.playlistmaker.medialibrary.data.repository

import android.content.SharedPreferences
import android.util.Log
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
    private val sharedPrefs: SharedPreferences,

    ) : MediaLibraryRepository {

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

    private fun convertFromPlaylistEntity(playlistEntity: List<PlaylistEntity>): List<Playlist> {
        return playlistEntity.map { playlistEntity ->
            converter.map(playlistEntity)
        }
    }

    override fun getAllTracksFromPlaylist(playlistId: Long): Flow<List<Track>> = flow {
        val trackListFlow = appDatabase.playlistDao().getAllTracksFromPlaylist(playlistId)
        trackListFlow.collect { trackList ->
            val reversedTracklist = trackList.reversed()
            emit(convertFromTrackEntity(reversedTracklist))
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playListFlow = appDatabase.playlistDao().getPlaylists()
        playListFlow.collect { playList ->
            val reversedPlaylist = playList.reversed()
            emit(convertFromPlaylistEntity(reversedPlaylist))
        }
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> = flow {
        val playListFlow = appDatabase.playlistDao().getPlaylistById(playlistId)

        playListFlow.collect { playList ->
            Log.d("PlaylistafterUpdate", "$playList")
            if (playList != null) {
                emit(converter.map(playList))
            }
        }
    }

//    override suspend fun getPlaylistByName(playlistName: String): Playlist {
//        return withContext(Dispatchers.IO) {
//            try {
//                val playlistEntity = appDatabase.playlistDao().getPlaylistByName(playlistName)
//                    ?: throw NullPointerException("Playlist not found")
//                converter.map(playlistEntity)
//            } catch (e: NullPointerException) {
//                Log.e("PlaylistRepository", "Error fetching playlist", e)
//                Playlist("", "", "".toUri(), 0, false)
//            }
//        }
//    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: String) {
        withContext(Dispatchers.IO) {
            appDatabase.playlistDao().deleteTrackFromPlaylist(playlistId, trackId)
        }
    }

    override suspend fun updatePlaylistTable(
        playListId: Long,
        newPlaylistName: String,
        newDescription: String,
        newCoverUri: String,
    ) {
        withContext(Dispatchers.IO){
            appDatabase.playlistDao().updatePlaylistTable(
                playListId,
                newPlaylistName,
                newDescription,
                newCoverUri
            )
        }
    }

//    override suspend fun updatePlaylist(
//        playlistId: Long,
//        newPlaylistName: String,
//        newDescription: String,
//        newCoverUri: String
//    ) {

//        withContext(Dispatchers.IO) {
//            appDatabase.playlistDao()
//                .updatePlaylistTable(oldPlaylistName, newPlaylistName, newDescription, newCoverUri)
//        }
//        appDatabase.playlistDao().updateCrossRefTable(oldPlaylistName, newPlaylistName)
//        val name = oldPlaylistName
//        val new = newPlaylistName
//        Log.d("oldPlaylistName","$name")
//        Log.d("newPlaylistName","$new")
//        appDatabase.playlistDao().updatePlaylistTable(oldPlaylistName, newPlaylistName, newDescription, newCoverUri)
//    }


    override suspend fun addPlaylistWithReplace(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            val playlistEntity = converter.map(playlist)
            appDatabase.playlistDao().addPlaylistWithReplace(playlistEntity)
        }
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            val playlistEntity = converter.map(playlist)
            appDatabase.playlistDao().addPlaylist(playlistEntity)
        }
    }

    override suspend fun deletePlaylist(playListId: Long) {
        withContext(Dispatchers.IO) {
            appDatabase.playlistDao().deletePlaylist(playListId)
        }
    }

//    override suspend fun deletePlaylist(playlist: Playlist) {
//        withContext(Dispatchers.IO) {
//            val playlistEntity = converter.map(playlist)
//            appDatabase.playlistDao().deletePlaylist(playlistEntity)
//        }
//    }

    private fun getPlaylistsFromStorage(): List<List<Track>>? {
        val playlistsGson: String? = sharedPrefs.getString(PLAYLIST_KEY, null)
        return playlistsGson?.let {
            val itemType = object : TypeToken<List<List<Track>>>() {}.type
            gson.fromJson(it, itemType)
        }
    }

    companion object {
        private const val PLAYLIST_KEY = "playlist_key"

    }
}