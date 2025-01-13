package com.practicum.playlistmaker.player.data.repository

import android.util.Log
import com.practicum.playlistmaker.AppDatabase
import com.practicum.playlistmaker.player.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.player.data.db.entity.PlaylistTracksCrossRef
import com.practicum.playlistmaker.player.data.db.entity.TrackEntity
import com.practicum.playlistmaker.player.data.utils.TrackDbConverter
import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.player.domain.repository.DatabaseRepository
import com.practicum.playlistmaker.player.domain.model.track_model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class DatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TrackDbConverter,
) : DatabaseRepository {

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

    override suspend fun updateIsFavoriteStatus(isFavorite: Boolean, track: Track) {
        val isInDataBase = appDatabase.playerTrackDao().isTrackInDataBase(track.trackId)
        if (isInDataBase) {
            appDatabase.playerTrackDao().updateIsFavoriteStatus(isFavorite, track.trackId)
        } else {
            appDatabase.playerTrackDao().insertTrack(converter.map(track))
            appDatabase.playerTrackDao().updateIsFavoriteStatus(isFavorite, track.trackId)
        }
    }

    override fun getFavoriteStatus(trackID: String): Flow<Boolean> = flow {
        val isInFavoriteFlow = appDatabase.playerTrackDao().getFavoriteStatus(trackID)
        isInFavoriteFlow.collect { isFavorite ->
            if (isFavorite == null) {
                emit(false)
            } else {
                emit(isFavorite)
            }
        }
    }

        override suspend fun getAllTracksFromPlaylist(playlistId: Long): List<Track> {
            val trackList = appDatabase.playerTrackDao().getAllTracksFromPlaylist(playlistId)
            val reversedTracklist = trackList.reversed()
            return convertFromTrackEntity(reversedTracklist)
        }

        override fun getPlaylists(): Flow<List<Playlist>> = flow {
            val playListFlow = appDatabase.playerTrackDao().getPlaylists()
            playListFlow.collect { playList ->
                val reversedPlaylist = playList.reversed()
                emit(convertFromPlaylistEntity(reversedPlaylist))
            }
        }

        override fun getPlaylistsWithTrackCount(): Flow<List<Playlist>> = flow {
            val playListsFlow = appDatabase.playerTrackDao().getPlaylistsWithTrackCount()
            playListsFlow.collect { playlist ->
                val reversedPlaylist = playlist.reversed()
                emit(convertFromPlaylistEntity(reversedPlaylist))
            }
        }

        override suspend fun insertPlayListTrackCrossRef(playlistId: Long, track: Track): Long {
            return if (appDatabase.playerTrackDao().isTrackInDataBase(track.trackId)) {
                appDatabase.playerTrackDao()
                    .insertPlayListTrackCrossRef(PlaylistTracksCrossRef(playlistId, track.trackId))
            } else {
                appDatabase.playerTrackDao().insertTrack(converter.map(track))
                appDatabase.playerTrackDao()
                    .insertPlayListTrackCrossRef(PlaylistTracksCrossRef(playlistId, track.trackId))
            }
        }

        override suspend fun checkPlaylistHasTrack(trackId: String, playlistId: Long): Boolean {
            return appDatabase.playerTrackDao().checkPlaylistHasTrack(trackId, playlistId)
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
    }