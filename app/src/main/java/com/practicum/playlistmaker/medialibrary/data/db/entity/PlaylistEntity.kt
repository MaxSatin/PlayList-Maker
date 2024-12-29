package com.practicum.playlistmaker.medialibrary.data.db.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlistName")
    val name: String,
    val description: String,
    val coverUri: String,
    val trackCount: Int,
    val containsCurrentTrack: Boolean
)
