package com.practicum.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.player.data.db.entity.TrackEntity


@Dao
interface PlayerTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun removeFromFavorite(track: TrackEntity)

}