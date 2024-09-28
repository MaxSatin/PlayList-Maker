package com.practicum.playlistmaker.search.presentation.state

import com.practicum.playlistmaker.search.domain.repository.TrackListRepository
import com.practicum.playlistmaker.search.domain.track_model.Track

sealed interface TrackListState {
    data object Loading : TrackListState

    data class Content(
        val tracks: List<Track>
    ): TrackListState

    data class Error(
        val errorMessage: String
    ): TrackListState

    data class NoConnection(
        val toastMessage: String
    ): TrackListState

    data class Empty(
        val message: String
    ): TrackListState

}