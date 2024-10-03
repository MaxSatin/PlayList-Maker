package com.practicum.playlistmaker.player.presentation.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
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
import com.practicum.playlistmaker.player.presentation.model.Track
import com.practicum.playlistmaker.player.presentation.state.PlayStatus
import com.practicum.playlistmaker.player.presentation.state.PlayerState

class PlayerViewModel(
    application: Application,
    playerInteractor: MediaPlayerInteractor,
    private val trackGson: String,
) : AndroidViewModel(application) {

    private val handler = Handler(Looper.getMainLooper())
    private val gson = GsonProvider.gson

    private val mediaPlayer = Creator.provideMediaPlayerInteractor()


    private var runnable: Runnable? = null

    private val screenStateLiveData = MutableLiveData<PlayerState>(PlayerState.Loading)
    private val playStatusLiveData = MutableLiveData<PlayStatus>()

    init {
        render(
            PlayerState.Content(
                loadTrackScreen(trackGson)
            )
        )
    }

    fun getScreenStateLiveData(): LiveData<PlayerState> = screenStateLiveData
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    private fun loadTrackScreen(track: String): Track {
        return gson.fromJson<Track>(track, Track::class.java)
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(0f, false)
    }

    fun startPlayer() {
        mediaPlayer.playerStart()
        showTimeCountDown()
    }

    private fun render(state: PlayerState) {
        screenStateLiveData.postValue(state)
    }

    private fun pausePlayer() {
        mediaPlayer.playerPause()
        val currentRunnable = runnable
        if (currentRunnable != null)
            handler.removeCallbacks(currentRunnable)

    }

    private fun showTimeCountDown() {
        val currentRunnable = runnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
        val newTimerRunnable = object : Runnable {
            override fun run() {
                binding.timePlayed.text =
                    DateFormatter.timeFormatter.format(mediaPlayer.getCurrentPosition())
                handler.postDelayed(this, TIMER_DELAY)
            }
        }

        this.runnable = newTimerRunnable

        handler.postDelayed(newTimerRunnable, TIMER_DELAY)

        mediaPlayer.setOnCompleteListener {
            handler.removeCallbacks(newTimerRunnable)
            binding.timePlayed.text = DateFormatter.timeFormatter.format(0)
            binding.stopPlayerButton.isChecked = false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.releasePlayer()
        val currentRunnable = runnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.stopPlayerButton.isChecked = false
        val currentRunnable = runnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
        mediaPlayer.playerPause()

    }

    companion object {
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val TIMER_DELAY = 50L

        fun getPlayerViewModelFactory(trackGson: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PlayerViewModel(
                        this[APPLICATION_KEY] as Application,
                        Creator.provideMediaPlayerInteractor(),
                        trackGson
                    )
                }
            }
    }
}

