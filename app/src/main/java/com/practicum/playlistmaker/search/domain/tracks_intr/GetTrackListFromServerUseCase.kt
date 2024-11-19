package com.practicum.playlistmaker.search.domain.tracks_intr

import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.track_model.Resourse
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.domain.repository.TrackListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTrackListFromServerUseCase(
    private val tracklistRepository: TrackListRepository
) {

    operator fun invoke(expression: String): Flow<ConsumerData<List<Track>>> {
            return tracklistRepository.getTrackList(expression).map { result ->
                when (result) {
                    is Resourse.Success -> ConsumerData.Data(result.data)
                    is Resourse.Error -> ConsumerData.Error(result.message)
                    is Resourse.NoConnection -> ConsumerData.NoConnection(result.message)

                }
            }
        }
    }