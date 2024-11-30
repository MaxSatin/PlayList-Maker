package com.practicum.playlistmaker.search.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity

@Dao
interface SearchDao {
    @Query("SELECT * FROM favorite_tracks_table WHERE track_id = :trackId")
    fun getTrackById(trackId: String): TrackEntity

}