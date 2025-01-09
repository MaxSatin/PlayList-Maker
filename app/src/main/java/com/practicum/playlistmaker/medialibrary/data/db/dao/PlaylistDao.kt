package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Transaction
    @Query(
        """
    SELECT
        p.playlistId AS playlistId,
        p.playlistName AS playlistName,
        p.description AS description,
        p.coverUri AS coverUri,
        COUNT(crossRef.trackId) AS trackCount,
        p.containsCurrentTrack AS containsCurrentTrack,
        p.timeStamp as timeStamp
    FROM playlist_table AS p
    LEFT JOIN playlistcrossref_table AS crossRef 
        ON p.playlistId = crossRef.playListId
    GROUP BY p.playlistId, p.playlistName, p.description, p.coverUri, p.trackCount, p.containsCurrentTrack
    ORDER BY p.timeStamp ASC
"""
    )
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Transaction
    @Query(
        "SELECT t.* FROM tracks_table t " +
                "INNER JOIN playlistcrossref_table j ON t.trackId = j.trackId " +
                "WHERE j.playlistId = :playListId"
    )
    fun getAllTracksFromPlaylist(playListId: Long): Flow<List<TrackEntity>>

    @Transaction
    @Query(
        """SELECT 
        p.playlistId AS playlistId,
        p.playlistName AS playlistName,
        p.description AS description,
        p.coverUri AS coverUri,
        COUNT(crossRef.trackId) AS trackCount,
        p.containsCurrentTrack AS containsCurrentTrack,
        p.timeStamp as timeStamp
    FROM playlist_table AS p
    LEFT JOIN playlistcrossref_table AS crossref
        ON p.playlistId = crossref.playListId
    WHERE p.playlistId = :playListId
    GROUP BY p.playlistName
    LIMIT 1
        """
    )
    fun getPlaylistById(playListId: Long): Flow<PlaylistEntity>
//    suspend fun getPlaylistByName(playListName: String): PlaylistEntity?


    @Query("DELETE FROM playlistcrossref_table WHERE playListId =:playListId AND trackId =:trackId")
    suspend fun deleteTrackFromPlaylist(playListId: Long, trackId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylistWithReplace(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

//    @Query(
//        """
//            UPDATE playlist_table
//            SET playlistName =:newPlaylistName
//            WHERE playlistName =:oldPlaylistName
//            """
//    )
//    suspend fun updatePlaylistTable(oldPlaylistName: String, newPlaylistName: String)

    @Transaction
    @Query(
        """UPDATE playlist_table
        SET 
        playlistName = CASE WHEN :newPlaylistName IS NOT NULL THEN :newPlaylistName ELSE playlistName END,
        description = CASE WHEN :newDescription IS NOT NULL THEN :newDescription ELSE description END,
        coverUri = CASE WHEN :newCoverUri IS NOT NULL THEN :newCoverUri ELSE coverUri END
    WHERE playlistId = :playListId
"""
    )
    suspend fun updatePlaylistTable(
        playListId: Long,
        newPlaylistName: String,
        newDescription: String,
        newCoverUri: String,
    )

//    @Transaction
//    @Query(
//        """
//            UPDATE playlistcrossref_table
//            SET playlistName =:newPlaylistName
//            WHERE playlistName =:oldPlaylistName
//        """
//    )
//    suspend fun updateCrossRefTable(oldPlaylistName: String, newPlaylistName: String)

//    @Transaction
//    suspend fun updateDependencies(
//        oldPlaylistName: String,
//        newPlaylistName: String,
//        newDescription: String,
//        newCoverUri: String,
//    ){
//        updateCrossRefTable(oldPlaylistName, newPlaylistName)
//        updatePlaylistTable(oldPlaylistName, newPlaylistName, newDescription, newCoverUri)
//    }
}