package com.practicum.playlistmaker.data.repository


import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.data.dto.TrackListRequest
import com.practicum.playlistmaker.data.dto.TrackListResponse
import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.domain.track_model.Resourse
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.domain.repository.TrackListRepository

class TracklistRepositoryImpl(
    private val tracklistNetworkClient: TracklistNetworkClient,
) : TrackListRepository {

    override fun getTrackList(expression: String): Resourse<List<Track>> {

        val trackListResponse = tracklistNetworkClient.doTrackRequest(TrackListRequest(expression))
        when {
            trackListResponse is TrackListResponse -> {
                val trackListDto = trackListResponse.results ?: emptyList<TrackDto>()
                val tracks = trackListDto.map { trackDto -> TrackMapper.map(trackDto) }
                return Resourse.Success(tracks)
            }
            trackListResponse.resultCode == 503 -> {
                return Resourse.NoConnection("Нет сети")
            }
            else -> {
                return Resourse.Error("Произошла сетевая ошибка")
            }
        }
    }
}
