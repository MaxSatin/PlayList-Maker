package com.practicum.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlistmaker.player.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.player.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.player.data.db.entity.TrackEntity
import com.practicum.playlistmaker.player.domain.model.track_model.Track
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayerTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun removeFromFavorite(track: TrackEntity)

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM tracks_table WHERE trackId =:trackId)")
    suspend fun isTrackInDataBase(trackId: String): Boolean


    @Transaction
    @Query(
        "SELECT t.* FROM tracks_table t " +
                "INNER JOIN playlistcrossref_table j ON t.trackId = j.trackId " +
                "WHERE j.playlistName = :playlistName"
    )
    suspend fun getAllTracksFromPlaylist(playlistName: String): List<TrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayListTrackCrossRef(crossRef: PlaylistTrackCrossRef):Long

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
    fun getPlaylistsWithTrackCount(): Flow<List<PlaylistEntity>>


    @Query(
        "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM playlist_table t " +
                "INNER JOIN playlistcrossref_table j ON t.playlistName = j.playlistName " +
                "WHERE j.trackId =:trackId AND t.playlistName = :playlistName)"
    )
    suspend fun checkPlaylistHasTrack(trackId: String, playlistName: String): Boolean

    //    @Query(
//        ""
//            SELECT EXISTS(
//            SELECT 1
//            FROM playlist_table t
//            INNER JOIN playlistcrossref_table j ON  = j.playlistName
//            )
//    "")
}