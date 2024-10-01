package com.practicum.playlistmaker.search.presentation.state

import com.practicum.playlistmaker.search.domain.track_model.Track
sealed interface TrackListState {

    sealed interface SearchListState : TrackListState {
        data object Loading : SearchListState

        data class Content(
            val tracks: List<Track>

        ) : SearchListState

        data class Error(
            val errorMessage: String
        ) : SearchListState

        data class NoConnection(
            val toastMessage: String
        ) : SearchListState

        data class Empty(
            val message: String
        ) : SearchListState

    }

    sealed interface HistoryListState : SearchListState {
        data object Loading : HistoryListState

        data class Content(
            val tracks: List<Track>
        ) : HistoryListState

        data class Empty(
            val message: String
        ) : HistoryListState

    }
}


