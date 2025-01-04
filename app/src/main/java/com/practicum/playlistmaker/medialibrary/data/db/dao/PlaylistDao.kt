package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistCrossRef
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query(
        """
    SELECT 
        p.playlistName AS playlistName,
        p.description AS description,
        p.coverUri AS coverUri,
        COUNT(crossRef.trackId) AS trackCount,
        p.containsCurrentTrack AS containsCurrentTrack,
        p.timeStamp as timeStamp
    FROM playlist_table AS p
    LEFT JOIN playlistcrossref_table AS crossRef 
        ON p.playlistName = crossRef.playlistName
    GROUP BY p.playlistName, p.description, p.coverUri, p.trackCount, p.containsCurrentTrack
    ORDER BY p.timeStamp ASC
"""
    )
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Transaction
    @Query(
        "SELECT t.* FROM tracks_table t " +
                "INNER JOIN playlistcrossref_table j ON t.trackId = j.trackId " +
                "WHERE j.playlistName = :playlistName"
    )
    fun getAllTracksFromPlaylist(playlistName: String): Flow<List<TrackEntity>>

    @Query("SELECT * FROM playlist_table WHERE playlistName =:playlistName LIMIT 1")
    suspend fun getPlaylistByName(playlistName: String): PlaylistEntity?

    @Query("DELETE FROM playlistcrossref_table WHERE playlistName =:playlistName AND trackId =:trackId")
    suspend fun deleteTrackFromPlaylist(playlistName: String, trackId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylistWithReplace(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query(
        """
            UPDATE playlist_table 
            SET playlistName =:newPlaylistName 
            WHERE playlistName =:oldPlaylistName
            """
    )
    suspend fun updatePlaylistTable(oldPlaylistName: String, newPlaylistName: String)

    @Query(
        """
            UPDATE playlistcrossref_table 
            SET playlistName =:newPlaylistName
            WHERE playlistName =:oldPlaylistName
        """
    )
    suspend fun updateCrossRefTable(oldPlaylistName: String, newPlaylistName: String)

}