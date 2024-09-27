package com.practicum.playlistmaker.player.presentation.mapper

import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.player.presentation.model.TrackInfoModel

// Аналогично, Маппер для непригодившихся сейчас моделей. Оставил на будущее.

object TrackInfoMapper {
    fun map (track: Track): TrackInfoModel {
        return TrackInfoModel(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country
        )
    }
}