package com.practicum.playlistmaker.player.presentation.state

import com.practicum.playlistmaker.player.presentation.model.TrackInfoModel


data class PlayerState(
    var isLoading: Boolean,
    val track: TrackInfoModel,
    val playStatus: PlayStatus
)
//sealed interface PlayerState {
//    object Loading : PlayerState
//    data class Content(val track: TrackInfoModel, val playStatus: PlayStatus) : PlayerState
////    data class Content(val track: TrackInfoModel): PlayerState
//}