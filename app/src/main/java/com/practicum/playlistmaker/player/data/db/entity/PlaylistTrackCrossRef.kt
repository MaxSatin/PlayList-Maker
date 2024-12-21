package com.practicum.playlistmaker.player.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "playlistcrossref_table",
    primaryKeys = ["playlistName", "trackId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["playlistName"],
            childColumns = ["playlistName"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = TrackEntity::class,
            parentColumns = ["trackId"],
            childColumns = ["trackId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class PlaylistTrackCrossRef(
    val playlistName: String,
    val trackId: String,
)
