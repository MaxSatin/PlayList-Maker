package com.practicum.playlistmaker.player.domain.mapper

import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.player.domain.model.TrackSearchModel

// Аналогично, Маппер для непригодившихся сейчас моделей. Оставил на будущее.
class TrackSearchMapper {
    fun map(track: Track): TrackSearchModel {
        return TrackSearchModel(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl60 = track.artworkUrl60
        )
    }
}


