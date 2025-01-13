package com.practicum.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlistmaker.player.data.db.entity.PlaylistTracksCrossRef
import com.practicum.playlistmaker.player.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.player.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayerTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun removeFromFavorite(track: TrackEntity)

    @Transaction
    @Query(
        """
            UPDATE tracks_table
            SET isFavorite =:isInFavorite
            WHERE trackId =:trackID
            """
    )
    suspend fun updateIsFavoriteStatus(isInFavorite:Boolean, trackID: String)

    @Query("SELECT isFavorite FROM tracks_table WHERE trackId =:trackID")
    fun getFavoriteStatus(trackID: String): Flow<Boolean?>

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM tracks_table WHERE trackId =:trackId)")
    suspend fun isTrackInDataBase(trackId: String): Boolean


    @Transaction
    @Query(
        "SELECT t.* FROM tracks_table t " +
                "INNER JOIN playlistcrossref_table j ON t.trackId = j.trackId " +
                "WHERE j.playListId = :playlistId"
    )
    suspend fun getAllTracksFromPlaylist(playlistId: Long): List<TrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayListTrackCrossRef(crossRef: PlaylistTracksCrossRef):Long

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
    GROUP BY p.playlistName, p.description, p.coverUri, p.trackCount, p.containsCurrentTrack
    ORDER BY p.timeStamp ASC
"""
    )
    fun getPlaylistsWithTrackCount(): Flow<List<PlaylistEntity>>


    @Query(
        "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM playlist_table t " +
                "INNER JOIN playlistcrossref_table j ON t.playlistId = j.playListId " +
                "WHERE j.trackId =:trackId AND t.playlistId = :playlistId)"
    )
    suspend fun checkPlaylistHasTrack(trackId: String, playlistId: Long): Boolean

}