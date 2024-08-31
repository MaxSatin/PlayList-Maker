package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.TrackListRequest
import com.practicum.playlistmaker.data.dto.TrackListResponse
import com.practicum.playlistmaker.data.mapper.TrackDtoToTrackMapper
import com.practicum.playlistmaker.data.storage.TracksHistoryStorage
import com.practicum.playlistmaker.domain.model.Resourse
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracklistRepository

class TracklistRepositoryImpl(
    private val tracklistNetWorkClient: TracklistNetworkClient,
    private val tracksHistoryStorage: TracksHistoryStorage,
    private val trackDtoToTrackMapper: TrackDtoToTrackMapper
) : TracklistRepository {

    override fun doTrackRequest(expression: String): Resourse<List<Track>> {

        val trackResponse = tracklistNetWorkClient.doTrackRequest(TrackListRequest(expression))
        return if (trackResponse is TrackListResponse) {
            Resourse.Success(trackResponse.results.map { trackDto ->
                trackDtoToTrackMapper.map(trackDto)
            })
        } else {
            Resourse.Error("Произошла сетевая ошибка")
        }
    }
}