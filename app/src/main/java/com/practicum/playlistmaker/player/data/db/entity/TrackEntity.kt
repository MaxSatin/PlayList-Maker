package com.practicum.playlistmaker.player.data.db.entity

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class TrackEntity(
    @PrimaryKey @ColumnInfo(name ="trackId")
    val trackId: String,
    val artistName: String,
    val trackName: String,
    val trackTimeMillis: Long,
    val previewUrl: String,
    val artworkUrl60: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    var isFavorite: Boolean,
    @Nullable
    val playListName: String?
)
