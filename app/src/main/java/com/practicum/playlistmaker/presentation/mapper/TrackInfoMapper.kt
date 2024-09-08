package com.practicum.playlistmaker.presentation.mapper

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.model.TrackInfo

class TrackInfoMapper {
    fun map (track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            previewUrl = track.previewUrl,
            artworkUrl60 = track.artworkUrl60,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country
        )
    }
}