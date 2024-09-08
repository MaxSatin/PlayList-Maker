package com.practicum.playlistmaker.presentation.mapper

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.model.TrackInfoModel

class TrackInfoMapper {
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