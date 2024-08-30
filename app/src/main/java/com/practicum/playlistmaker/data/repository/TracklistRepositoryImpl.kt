package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.TrackListRequest
import com.practicum.playlistmaker.data.dto.TrackListResponse
import com.practicum.playlistmaker.domain.model.Resourse
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracklistRepository

class TracklistRepositoryImpl(
    private val tracklistNetWorkClient: TracklistNetworkClient
) : TracklistRepository {
    override fun doTrackRequest(expression: String): Resourse<List<Track>> {

        val trackResponse = tracklistNetWorkClient.doTrackRequest(TrackListRequest(expression))
        return if (trackResponse is TrackListResponse) {
            Resourse.Success(trackResponse.results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.previewUrl,
                    it.artworkUrl60,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country
                )
            })

        } else {
            Resourse.Error("Произошла сетевая ошибка")
        }
    }
}