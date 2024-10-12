package com.practicum.playlistmaker.player.presentation.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.practicum.playlistmaker.player.domain.player_interactor.MediaPlayerInteractor
import com.practicum.playlistmaker.player.presentation.mapper.DateFormatter
import com.practicum.playlistmaker.player.presentation.mapper.TrackInfoMapper
import com.practicum.playlistmaker.player.presentation.model.Track
import com.practicum.playlistmaker.player.presentation.state.PlayStatus
import com.practicum.playlistmaker.player.presentation.state.PlayerState

class PlayerViewModel(
    application: Application,
    trackGson: String?,
    private val playerInteractor: MediaPlayerInteractor,
    private val gson: Gson
) : AndroidViewModel(application) {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val trackItem = loadTrackScreen(trackGson)

    private val playerStateLiveData = MutableLiveData<PlayerState>()

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
                progress =  DateFormatter.timeFormatter.format(0),
                isPlaying= false)
        )
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
        if (this.runnable != null)
            handler.removeCallbacksAndMessages(TRACK_TIMER_TOKEN)
        val newState = getCurrentPlayerState()
        playerStateLiveData.value = newState.copy(
            playStatus = newState.playStatus.copy(isPlaying = false)
        )
    }

    private fun showTimeCountDown() {

        val newTimerRunnable = object : Runnable {
            override fun run() {
                val newState = getCurrentPlayerState()
                playerStateLiveData.value = newState.copy(
                    playStatus = newState.playStatus.copy(
                        progress = DateFormatter.timeFormatter.format(playerInteractor.getCurrentPosition()),
                        isPlaying = playerInteractor.isPlaying()
                    )
                )
                val postTime = SystemClock.uptimeMillis() + TIMER_DELAY
                handler.postAtTime(
                    this,
                    TRACK_TIMER_TOKEN,
                    postTime
                )
            }
        }

        this.runnable = newTimerRunnable

        handler.postDelayed(newTimerRunnable, TIMER_DELAY)

        playerInteractor.setOnCompleteListener {
            handler.removeCallbacksAndMessages(TRACK_TIMER_TOKEN)

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
        handler.removeCallbacksAndMessages(TRACK_TIMER_TOKEN)
    }

    companion object {
        private const val TIMER_DELAY = 50L
        private val TRACK_TIMER_TOKEN = Any()

    }
}

