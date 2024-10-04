package com.practicum.playlistmaker.player.presentation.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.Creator.GsonProvider
import com.practicum.playlistmaker.player.domain.player_interactor.MediaPlayerInteractor
import com.practicum.playlistmaker.player.presentation.mapper.DateFormatter
import com.practicum.playlistmaker.player.presentation.mapper.TrackInfoMapper
import com.practicum.playlistmaker.player.presentation.model.Track
import com.practicum.playlistmaker.player.presentation.state.PlayStatus
import com.practicum.playlistmaker.player.presentation.state.PlayerState

class PlayerViewModel(
    application: Application,
    private val playerInteractor: MediaPlayerInteractor,
    private val trackGson: String?,
) : AndroidViewModel(application) {

    private val handler = Handler(Looper.getMainLooper())
    private val gson = GsonProvider.gson
    private var runnable: Runnable? = null
    private var trackTimer: String? = null
    private val trackItem = loadTrackScreen(trackGson)

    private val screenStateLiveData = MutableLiveData<PlayerState>(PlayerState.Loading)


    private val playStatusLiveData = MutableLiveData<PlayStatus>()

    init {
        render(PlayerState.Loading)
        preparePlayer()
        render(
            PlayerState.Content(
                TrackInfoMapper.map(trackItem)
            )
        )
    }

    fun getScreenStateLiveData(): LiveData<PlayerState> = screenStateLiveData
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    private fun loadTrackScreen(track: String?): Track {
        val trackItem = gson.fromJson<Track>(track, Track::class.java)
        return trackItem
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(
            DateFormatter.timeFormatter.format(0),
            false
        )
    }

    fun playerController() {
//        playerInteractor.playBackControll()
//        if(playerInteractor.isPlaying()){
//            showTimeCountDown()
//            getCurrentPlayStatus().copy(isPlaying = true)
//        }
        if (playerInteractor.isPlaying()){
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun preparePlayer(){
        playerInteractor.preparePlayer(trackItem.previewUrl)
    }

    fun startPlayer() {
        playerInteractor.playerStart()
        showTimeCountDown()
        playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
    }

    private fun render(state: PlayerState) {
        screenStateLiveData.postValue(state)
    }

    fun pausePlayer() {
        playerInteractor.playerPause()
        if (this.runnable != null)
            handler.removeCallbacksAndMessages(TRACK_TIMER_TOKEN)
        playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)

    }

    private fun showTimeCountDown() {
//        if (this.runnable != null) {
//            handler.removeCallbacksAndMessages(TRACK_TIMER_TOKEN)
//        }
        val newTimerRunnable = object : Runnable {
            override fun run() {
                playStatusLiveData.value = getCurrentPlayStatus().copy(
                    DateFormatter.timeFormatter.format(playerInteractor.getCurrentPosition()),
                    playerInteractor.isPlaying()
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
//        handler.removeCallbacksAndMessages(TRACK_TIMER_TOKEN)

        playerInteractor.setOnCompleteListener {
            handler.removeCallbacksAndMessages(TRACK_TIMER_TOKEN)
//            trackTimer = DateFormatter.timeFormatter.format(0)
            playStatusLiveData.value = getCurrentPlayStatus()
                .copy(isPlaying = false,
                    progress =  DateFormatter.timeFormatter.format(0)
                )
//            binding.timePlayed.text = DateFormatter.timeFormatter.format(0)
//            binding.stopPlayerButton.isChecked = false
        }
    }


    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
        handler.removeCallbacksAndMessages(TRACK_TIMER_TOKEN)
    }
//    () {
////        super.onDestroy()
//
//
////        val currentRunnable = runnable
////        if (currentRunnable != null) {
////            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
////        }
//    }

//    override fun onPause() {
//        super.onPause()
//        binding.stopPlayerButton.isChecked = false
//        val currentRunnable = runnable
//        if (currentRunnable != null) {
//            handler.removeCallbacksAndMessages(TRACK_TIMER_TOKEN)
//        }
//        playerInteractor.playerPause()
//
//    }

    companion object {
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val TIMER_DELAY = 50L
        private val TRACK_TIMER_TOKEN = Any()

        fun getPlayerViewModelFactory(trackGson: String?): ViewModelProvider.Factory =
            viewModelFactory {
                val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
                initializer {
                    PlayerViewModel(
                        this[APPLICATION_KEY] as Application,
                        mediaPlayerInteractor,
                        trackGson
                    )
                }
            }
    }
}

