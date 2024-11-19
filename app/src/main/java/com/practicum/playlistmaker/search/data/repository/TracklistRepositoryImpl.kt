package com.practicum.playlistmaker.search.data.repository


import com.practicum.playlistmaker.search.data.dto.TrackListRequest
import com.practicum.playlistmaker.search.data.dto.TrackListResponse
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.domain.track_model.Resourse
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.domain.repository.TrackListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracklistRepositoryImpl(
    private val tracklistNetworkClient: TracklistNetworkClient,
) : TrackListRepository {

    override fun getTrackList(expression: String): Flow<Resourse<List<Track>>> = flow {
        val trackListResponse = tracklistNetworkClient.doTrackRequest(TrackListRequest(expression))
        when (trackListResponse.resultCode) {
            -1 -> emit(Resourse.NoConnection("Проверьте подключение к интернету"))
            200 -> with(trackListResponse as TrackListResponse) {
                val data = results.map { trackDto ->
                    TrackMapper.map(trackDto)
                }
                emit(Resourse.Success(data))
            }
//            trackListResponse is TrackListResponse -> {
//                val trackListDto = trackListResponse.results ?: emptyList<TrackDto>()
//                val tracks = trackListDto.map { trackDto -> TrackMapper.map(trackDto) }
//                return Resourse.Success(tracks)
//            }
//            trackListResponse.resultCode == 503 -> {
            503 -> emit(Resourse.NoConnection("Нет сети"))
            else -> emit(Resourse.Error("Произошла сетевая ошибка"))

        }
    }
}


