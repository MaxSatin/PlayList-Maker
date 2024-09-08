package com.practicum.playlistmaker.presentation.mapper

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.model.TrackInfoModel
import com.practicum.playlistmaker.presentation.model.TrackSearchModel

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


