package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteTracklistDao {

    @Query("SELECT * FROM tracks_table WHERE isFavorite = 1")
    fun getFavoriteTrackList(): Flow<List<TrackEntity>>

}