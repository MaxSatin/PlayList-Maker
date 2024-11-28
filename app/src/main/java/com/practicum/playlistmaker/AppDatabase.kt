package com.practicum.playlistmaker

import androidx.room.Database
import com.practicum.playlistmaker.player.data.db.dao.TrackDao
import com.practicum.playlistmaker.player.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase {

    abstract fun trackDao(): TrackDao

}