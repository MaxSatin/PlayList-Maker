package com.practicum.playlistmaker.player.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey @ColumnInfo(name = "playlistName")
    val name: String,
    val description: String,
    val coverUri: String,
    val trackCount: Int,
    val containsCurrentTrack: Boolean
)
