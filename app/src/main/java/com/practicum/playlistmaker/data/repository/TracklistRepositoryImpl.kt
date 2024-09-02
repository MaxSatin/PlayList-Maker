package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.NetworkResponse
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.data.dto.TrackListRequest
import com.practicum.playlistmaker.data.dto.TrackListResponse
import com.practicum.playlistmaker.data.mapper.TrackDtoToTrackMapper
import com.practicum.playlistmaker.domain.consumer.ConsumerData
import com.practicum.playlistmaker.domain.model.Resourse
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracklistRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TracklistRepositoryImpl(
    private val tracklistNetworkClient: TracklistNetworkClient,
) : TracklistRepository {

    override fun getTrackList(expression: String): Resourse<List<Track>> {

        val trackListResponse = tracklistNetworkClient.doTrackRequest(TrackListRequest(expression))
        return if (trackListResponse is TrackListResponse) {
            val trackListDto = trackListResponse.results ?: emptyList<TrackDto>()
            val tracks = trackListDto.map { trackDto -> TrackDtoToTrackMapper.map(trackDto) }
            Resourse.Success(tracks)
        } else {
            Resourse.Error("Произошла сетевая ошибка")
        }
    }
}


//object : Callback<TrackListResponse> {
//    override fun onResponse(
//        call: Call<TrackListResponse>,
//        response: Response<TrackListResponse>
//    ) {
//        val trackDto = response.body()?.results ?: emptyList<TrackDto>()
//        val tracks = trackDto.map { trackDto -> TrackDtoToTrackMapper.map(trackDto) }
//        tracs = Resourse.Success(tracks)
//    }
//    override fun onFailure(call: Call<TrackListResponse>, t: Throwable) {
//        Resourse.Error(t)
//    }
//})