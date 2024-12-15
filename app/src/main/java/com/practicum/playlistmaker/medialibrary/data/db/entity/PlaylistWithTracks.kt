package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.Embedded

data class PlaylistWithTracks(
    @Embedded val playlistEntity: PlaylistEntity
)
