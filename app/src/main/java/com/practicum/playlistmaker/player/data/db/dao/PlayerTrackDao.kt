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

    @Transaction
    @Query(
        "SELECT t.* FROM tracks_table t " +
                "INNER JOIN playlistcrossref_table j ON t.trackId = j.trackId " +
                "WHERE j.playlistName = :playlistName"
    )
    suspend fun getAllTracksFromPlaylist(playlistName: String): List<TrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayListTrackCrossRef(crossRef: PlaylistTrackCrossRef)


}