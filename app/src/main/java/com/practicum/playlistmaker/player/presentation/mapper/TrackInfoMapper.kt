package com.practicum.playlistmaker.player.presentation.mapper

import com.practicum.playlistmaker.player.presentation.model.Track
import com.practicum.playlistmaker.player.presentation.model.TrackInfoModel


// TrackInfoModel практически ничем не отличается от основной модели Track за исключением полей trackTimeMillis и releaseDate
// тут сразу форматируем их, чтобы вынести логику из Activity

object TrackInfoMapper {
    fun map (track: Track): TrackInfoModel {
        return TrackInfoModel(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = DateFormatter.timeFormatter.format(track.trackTimeMillis),
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = DateFormatter.yearFormatter.format(track.trackTimeMillis),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            isInFavorite = track.isInFavorite
        )
    }
}