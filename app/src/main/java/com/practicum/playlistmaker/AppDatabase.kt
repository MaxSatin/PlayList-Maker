package com.practicum.playlistmaker

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.medialibrary.data.db.dao.MediaLibraryTrackDao
import com.practicum.playlistmaker.player.data.db.dao.PlayerTrackDao
import com.practicum.playlistmaker.player.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase(){

    abstract fun playerTrackDao(): PlayerTrackDao
    abstract fun mediaLibraryTrackDao(): MediaLibraryTrackDao

}