//package com.practicum.playlistmaker.search.presentation.state
//
//import com.practicum.playlistmaker.search.domain.track_model.Track
//
//sealed interface HistoryTrackListState {
//    data object Loading : HistoryTrackListState
//
//    data class Content(
//        val tracks: List<Track>
//    ): HistoryTrackListState
//
//    data class Empty(
//        val message: String
//    ): HistoryTrackListState
//
//}
