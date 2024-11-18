package com.practicum.playlistmaker.search.domain.tracks_intr

import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.track_model.Resourse
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.domain.repository.TrackListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class GetTrackListFromServerUseCase(
    private val tracklistRepository: TrackListRepository
) {

    private val executor = Executors.newCachedThreadPool()

    operator fun invoke(expression: String): Flow<ConsumerData<List<Track>>> {
            return tracklistRepository.getTrackList(expression).map { result ->
                when (result) {
//                when (val trackListResponse = tracklistRepository.getTrackList(expression)) {
                    is Resourse.Success -> ConsumerData.Data(result.data)
//                        consumer.consume(ConsumerData.Data(trackListResponse.data))
                    is Resourse.Error -> ConsumerData.Error(result.message)
//                        consumer.consume(ConsumerData.Error(trackListResponse.message))
                    is Resourse.NoConnection -> ConsumerData.NoConnection(result.message)
//                        consumer.consume(ConsumerData.NoConnection(trackListResponse.message))
                }
            }
        }
    }