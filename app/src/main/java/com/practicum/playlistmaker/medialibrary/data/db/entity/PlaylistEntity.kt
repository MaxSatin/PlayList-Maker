package com.practicum.playlistmaker.medialibrary.data.db.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity (tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey @ColumnInfo(name = "playlistName")
    val name: String,
    val description: String,
    val coverUri: String,
    val trackCount: Int,
    val containsCurrentTrack: Boolean,
    val timeStamp:Long = System.currentTimeMillis()
)
//    @Relation(
//        parentColumn = "playlistName",
//        entityColumn = "trackId",
//        associateBy = Junction(PlaylistCrossRef::class)
//    )