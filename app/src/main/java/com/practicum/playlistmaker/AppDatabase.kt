package com.practicum.playlistmaker

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.medialibrary.data.db.dao.FavoriteTracklistDao
import com.practicum.playlistmaker.medialibrary.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistCrossRef
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.player.data.db.dao.PlayerTrackDao
import com.practicum.playlistmaker.player.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.player.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.data.db.dao.SearchDao

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackCrossRef::class])
abstract class AppDatabase: RoomDatabase(){


    abstract fun favoriteTracklistDao(): FavoriteTracklistDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playerTrackDao(): PlayerTrackDao
    abstract fun searchTrackDao(): SearchDao

}