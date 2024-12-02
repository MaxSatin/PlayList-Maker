package com.practicum.playlistmaker.search.presentation.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.domain.tracks_intr.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.ClearHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackHistoryFromStorageUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackListFromServerUseCase
import com.practicum.playlistmaker.search.presentation.state.State
import com.practicum.playlistmaker.search.presentation.utils.SingleEventLifeData
import com.practicum.playlistmaker.search.presentation.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
    private val getTrackList: GetTrackListFromServerUseCase,
    private val addTrackToHistory: AddTrackToHistoryUseCase,
    private val clearHistory: ClearHistoryUseCase,
    private val getTracksHistory: GetTrackHistoryFromStorageUseCase,
    private val gson: Gson,
) : AndroidViewModel(application) {

    private var isClickAllowed = true
    private var emptyTrackList = emptyList<Track>()

    private val handler = Handler(Looper.getMainLooper())

    private val debounceSearch = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    )
    { query ->
        searchTracks(query)
    }

    private val setIsClickAllowed = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        viewModelScope,
        useLastParam = false
    )
    { isAllowed ->
        isClickAllowed = isAllowed
    }

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
        viewModelScope.launch {
            getTracksHistory.collect { trackList ->

                if (trackList.isNullOrEmpty()) {
                    renderHistoryState(State.HistoryListState.Empty("Список пуст"))
                } else {
                    renderHistoryState(State.HistoryListState.Content(trackList))
                }

            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            setIsClickAllowed(true)
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
        viewModelScope.launch {
            addTrackToHistory(track = track)
        }
            updateHistoryList()
    }

    private fun updateHistoryList() {
        viewModelScope.launch {
            getTracksHistory().collect { trackList ->
                historyStateLiveData.value = State.HistoryListState.Content(trackList)
            }
        }
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchedText == changedText) {
            return
        }
        this.latestSearchedText = changedText
        debounceSearch(changedText)
    }

    fun searchTracks(query: String) {
        if (query.isNotEmpty()) {
            renderState(
                State.SearchListState.Loading
            )
        }
        viewModelScope.launch {
            getTrackList(query)
                .collect { consumerData ->
                    processResult(consumerData)
                }
        }
    }

    private fun processResult(data: ConsumerData<List<Track>>) {
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
                    Log.d(
                        "trackListViewModel",
                        "${renderState(State.SearchListState.Content(data.value))}"
                    )
                }
            }
        }

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