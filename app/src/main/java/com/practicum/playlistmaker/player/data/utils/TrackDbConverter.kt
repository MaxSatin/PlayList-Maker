package com.practicum.playlistmaker.player.data.utils

import com.practicum.playlistmaker.player.data.db.entity.TrackEntity
import com.practicum.playlistmaker.player.presentation.model.Track


class TrackDbConverter {

    fun map(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
                trackId,
                artistName,
                trackName,
                trackTimeMillis,
                previewUrl,
                artworkUrl60,
                artworkUrl100,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                isFavorite,
                null
            )
        }
    }
}