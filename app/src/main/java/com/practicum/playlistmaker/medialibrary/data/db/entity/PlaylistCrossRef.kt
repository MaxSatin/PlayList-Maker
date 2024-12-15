package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["track_id", "playlist_name"])
data class PlaylistCrossRef(
    val trackId: String,
    val playlistName: String
)
