package com.practicum.playlistmaker.search.presentation.state

import com.practicum.playlistmaker.search.domain.track_model.Track

sealed interface State {

    sealed interface SearchListState : State {
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

    sealed interface HistoryListState : State {
        data class Content(
            val tracks: List<Track>
        ) : HistoryListState

        data class Empty(
            val message: String
        ) : HistoryListState
    }
}


