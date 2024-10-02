package com.practicum.playlistmaker.player.presentation.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.Creator.GsonProvider
import com.practicum.playlistmaker.player.presentation.mapper.DateFormatter
import com.practicum.playlistmaker.search.domain.track_model.Track

class PlayerViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val handler = Handler(Looper.getMainLooper())
    private val gson = GsonProvider.gson

    private val mediaPlayer = Creator.provideMediaPlayerInteractor()

    lateinit var trackItem: Track

    private var runnable: Runnable? = null

    private fun startPlayer() {
        mediaPlayer.playerStart()
        showTimeCountDown()
        binding.stopPlayerButton.isChecked = true
    }

    private fun pausePlayer() {
        mediaPlayer.playerPause()
        val currentRunnable = runnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
        binding.stopPlayerButton.isChecked = false
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
    }

}