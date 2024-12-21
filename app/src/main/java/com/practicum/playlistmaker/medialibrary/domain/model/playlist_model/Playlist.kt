package com.practicum.playlistmaker.medialibrary.domain.model.playlist_model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Playlist(
    val name: String,
    val description: String,
    val coverUri: Uri?,
    var tracksNumber: Int
)
