package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.consumer.ConsumerData
import com.practicum.playlistmaker.domain.model.Resourse
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.interactors.SearchTrackListInteractor
import com.practicum.playlistmaker.domain.repository.TrackListRepository
import java.util.concurrent.Executors

class GetTrackListFromServerUseCase(
    private val tracklistRepository: TrackListRepository
) : SearchTrackListInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: Consumer<List<Track>>) {
            executor.execute {
                when (val trackListResponse = tracklistRepository.getTrackList(expression)) {
                    is Resourse.Success -> consumer.consume(ConsumerData.Data(trackListResponse.data))
                    is Resourse.Error -> consumer.consume(ConsumerData.Error(trackListResponse.message))
                }
            }
        }
    }