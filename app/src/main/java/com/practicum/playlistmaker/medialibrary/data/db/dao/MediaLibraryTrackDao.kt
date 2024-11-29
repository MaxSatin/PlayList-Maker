package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MediaLibraryTrackDao {

    @Query("SELECT * FROM favorite_tracks_table")
    suspend fun getFavoriteTrackList(): List<TrackEntity>

}