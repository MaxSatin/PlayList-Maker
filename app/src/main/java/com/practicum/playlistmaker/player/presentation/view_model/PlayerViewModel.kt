package com.practicum.playlistmaker.player.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.player.domain.db_interactor.DatabaseInteractor
import com.practicum.playlistmaker.player.domain.player_interactor.MediaPlayerInteractor
import com.practicum.playlistmaker.player.presentation.mapper.DateFormatter
import com.practicum.playlistmaker.player.presentation.mapper.TrackInfoMapper
import com.practicum.playlistmaker.player.presentation.model.Track
import com.practicum.playlistmaker.player.presentation.state.PlayStatus
import com.practicum.playlistmaker.player.presentation.state.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    application: Application,
    trackGson: String?,
    private val databaseInteractor: DatabaseInteractor,
    private val playerInteractor: MediaPlayerInteractor,
    private val gson: Gson,
) : AndroidViewModel(application) {

    private val trackItem = loadTrackScreen(trackGson)

    private val playerStateLiveData = MutableLiveData<PlayerState>()

    private var timerJob: Job? = null

    init {
        showLoading()
        loadContent()
        preparePlayer()
    }

    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private fun loadTrackScreen(track: String?): Track {
        val trackItem = gson.fromJson<Track>(track, Track::class.java)
        return trackItem
    }

    private fun getCurrentPlayerState(): PlayerState {
        return playerStateLiveData.value ?: PlayerState(
            isLoading = false,
            track = TrackInfoMapper.map(trackItem),
            playStatus = PlayStatus(
                isPrepared = false,
                progress = DateFormatter.timeFormatter.format(0),
                isPlaying = false
            )
        )
    }

    fun saveTrackToFavorites() {
        viewModelScope.launch {
            trackItem.isInFavorite = true
            databaseInteractor.saveTrackToDatabase(trackItem)
        }
    }

    fun playerController() {
        if (playerInteractor.isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun loadContent() {
        playerStateLiveData.value = getCurrentPlayerState().copy(
            track = TrackInfoMapper.map(trackItem),
        )
    }

    fun showLoading() {
        playerStateLiveData.value = getCurrentPlayerState().copy(
            isLoading = true,
        )
    }

    fun preparePlayer() {
        playerInteractor.preparePlayer(trackItem.previewUrl)
        playerInteractor.setOnPreparedListener {
            val newState = getCurrentPlayerState()
            playerStateLiveData.value = newState.copy(
                isLoading = false,
                playStatus = newState.playStatus.copy(isPrepared = true)
            )
        }
    }

    fun startPlayer() {
        playerInteractor.playerStart()
        showTimeCountDown()
        val newState = getCurrentPlayerState()
        playerStateLiveData.value = newState.copy(
            playStatus = newState.playStatus.copy(isPlaying = true)
        )
    }

    fun pausePlayer() {
        playerInteractor.playerPause()
        timerJob?.cancel()
        val newState = getCurrentPlayerState()
        playerStateLiveData.value = newState.copy(
            playStatus = newState.playStatus.copy(isPlaying = false)
        )
    }

    private fun showTimeCountDown() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(TIMER_DELAY)
                val newState = getCurrentPlayerState()
                playerStateLiveData.value = newState.copy(
                    playStatus = newState.playStatus.copy(
                        progress = DateFormatter.timeFormatter.format(playerInteractor.getCurrentPosition()),
                        isPlaying = playerInteractor.isPlaying()
                    )
                )

            }
        }

        playerInteractor.setOnCompleteListener {
            timerJob?.cancel()

            playerInteractor.seekTo(0)

            val onTrackComplete = getCurrentPlayerState()
            playerStateLiveData.value = onTrackComplete.copy(
                playStatus = onTrackComplete.playStatus.copy(
                    progress = DateFormatter.timeFormatter.format(0)
                )
            )

            val onPlayerComplete = getCurrentPlayerState()
            playerStateLiveData.value = onPlayerComplete.copy(
                playStatus = onPlayerComplete.playStatus.copy(
                    isPlaying = false
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
    }

    companion object {
        private const val TIMER_DELAY = 50L
        private val TRACK_TIMER_TOKEN = Any()

    }
}

