package com.practicum.playlistmaker.search.presentation.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.gson.Gson
//import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.domain.tracks_intr.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.CheckIsHistoryEmptyUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.ClearHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackHistoryFromStorageUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackListFromServerUseCase
import com.practicum.playlistmaker.search.presentation.state.State
import com.practicum.playlistmaker.search.presentation.utils.SingleEventLifeData

class SearchViewModel(
    application: Application,
    private val getTrackList: GetTrackListFromServerUseCase,
    private val addTrackToHistory: AddTrackToHistoryUseCase,
    private val clearHistory: ClearHistoryUseCase,
    private val getTracksHistory: GetTrackHistoryFromStorageUseCase,
    private val checkIsHistoryEmpty: CheckIsHistoryEmptyUseCase,
    private val gson: Gson
) : AndroidViewModel(application) {

    private var isClickAllowed = true
    private var emptyTrackList = emptyList<Track>()

    private val handler = Handler(Looper.getMainLooper())

    private val trackList = mutableListOf<Track>()
    private var latestSearchedText: String? = null

    private val stateLiveData = MutableLiveData<State.SearchListState>()
    private val historyStateLiveData = MutableLiveData<State.HistoryListState>()

    private val showTrackPlayerTrigger = SingleEventLifeData<String>()

    fun observeHistoryState(): LiveData<State.HistoryListState> = historyStateLiveData
    fun observeTrackSearchState(): LiveData<State.SearchListState> = stateLiveData
    fun getShowTrackPlayerTrigger(): LiveData<String> = showTrackPlayerTrigger

    init {
        getHistoryTracks()
    }

    fun getHistoryTracks() {

        val getTracksHistory = getTracksHistory()
        if (getTracksHistory.isNullOrEmpty()) {
            renderHistoryState(State.HistoryListState.Empty("Список пуст"))
        } else {
            renderHistoryState(State.HistoryListState.Content(getTracksHistory))
        }

    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun showTrackPlayer(track: Track) {
        if (clickDebounce()) {
            addTrackToHistoryList(track)
            val track = gson.toJson(track)
            showTrackPlayerTrigger.value = track
        }
    }

    fun clearHistoryList() {
        clearHistory()
        updateHistoryList()
    }

    fun clearSearchList() {
        renderState(
            State.SearchListState.Content(
                emptyTrackList
            )
        )
    }

    private fun addTrackToHistoryList(track: Track) {
        addTrackToHistory(track = track)
        updateHistoryList()
    }

    private fun updateHistoryList() {
            historyStateLiveData.value = State.HistoryListState.Content(getTracksHistory())

    }

    fun searchDebounce(changedText: String) {
        if (latestSearchedText == changedText) {
            return
        }

        this.latestSearchedText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchTracks(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    fun searchTracks(query: String) {
        if (query.isNotEmpty()) {
            renderState(
                State.SearchListState.Loading
            )
        }
        getTrackList(
            expression = query,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data) {
                        is ConsumerData.Error -> renderState(State.SearchListState.Error(data.message))
                        is ConsumerData.NoConnection -> renderState(
                            State.SearchListState.NoConnection(
                                data.message
                            )
                        )

                        is ConsumerData.Data -> {
                            if (data.value.isNullOrEmpty()) {
                                renderState(State.SearchListState.Empty("По запросу ничего не нашлось"))
                            } else {
                                renderState(State.SearchListState.Content(data.value))
                            }
                        }
                    }
                }
            }
        )

    }

    private fun renderState(state: State) {
        when (state) {
            is State.SearchListState -> stateLiveData.postValue(state)
            is State.HistoryListState -> historyStateLiveData.postValue(state)
        }
    }

    private fun renderHistoryState(historyState: State.HistoryListState) {
        historyStateLiveData.postValue(historyState)
    }


    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()


        private const val KEY = "KEY"
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }
}