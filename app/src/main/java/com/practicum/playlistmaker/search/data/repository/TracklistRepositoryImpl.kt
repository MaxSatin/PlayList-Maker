package com.practicum.playlistmaker.search.data.repository


import com.practicum.playlistmaker.AppDatabase
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
    private val appDatabase: AppDatabase
) : TrackListRepository {

    override fun getTrackList(expression: String): Flow<Resourse<List<Track>>> = flow {

        val favoriteTracksIdsFlow = appDatabase.searchTrackDao().getFavoriteTracksId()

        favoriteTracksIdsFlow.collect { favoriteIds ->
            val trackListResponse =
                tracklistNetworkClient.doTrackRequest(TrackListRequest(expression))
            when (trackListResponse.resultCode) {
                -1 -> emit(Resourse.NoConnection("Проверьте подключение к интернету"))
                200 -> with(trackListResponse as TrackListResponse) {
                    val data = results.map { trackDto ->
                        val track = TrackMapper.map(trackDto)
                        track.isFavorite = favoriteIds.contains(track.trackId)
                        track
                    }
                    emit(Resourse.Success(data))
                }

                503 -> emit(Resourse.NoConnection("Нет сети"))
                else -> emit(Resourse.Error("Произошла сетевая ошибка"))

            }
        }
    }
}


