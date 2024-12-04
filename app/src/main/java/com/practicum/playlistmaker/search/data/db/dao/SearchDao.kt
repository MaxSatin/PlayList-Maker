package com.practicum.playlistmaker.search.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Query("SELECT track_id FROM favorite_tracks_table")
    fun getFavoriteTracksId(): Flow<List<String>>

}