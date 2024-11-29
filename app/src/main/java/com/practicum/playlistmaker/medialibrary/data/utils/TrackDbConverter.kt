package com.practicum.playlistmaker.medialibrary.data.utils

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity

class TrackDbConverter {

    fun map(trackEntity: TrackEntity): Track {
        return with(trackEntity){
            Track(
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
                isInFavorite
            )
        }
    }
}