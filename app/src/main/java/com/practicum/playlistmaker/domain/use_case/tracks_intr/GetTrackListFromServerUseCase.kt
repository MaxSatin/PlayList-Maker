package com.practicum.playlistmaker.domain.use_case.tracks_intr

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.consumer.ConsumerData
import com.practicum.playlistmaker.domain.model.Resourse
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TrackListRepository
import java.util.concurrent.Executors

class GetTrackListFromServerUseCase(
    private val tracklistRepository: TrackListRepository
) {

    private val executor = Executors.newCachedThreadPool()

    operator fun invoke(expression: String, consumer: Consumer<List<Track>>) {
            executor.execute {
                when (val trackListResponse = tracklistRepository.getTrackList(expression)) {
                    is Resourse.Success -> consumer.consume(ConsumerData.Data(trackListResponse.data))
                    is Resourse.Error -> consumer.consume(ConsumerData.Error(trackListResponse.message))
                    is Resourse.NoConnection -> consumer.consume(ConsumerData.NoConnection(trackListResponse.message))
                }
            }
        }
    }