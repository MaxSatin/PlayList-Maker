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
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.Creator.GsonProvider
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.presentation.state.HistoryTrackListState
import com.practicum.playlistmaker.search.presentation.state.TrackListState

class SearchViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val getTrackList = Creator.provideGetTrackListFromServerUseCase()
    private val addTrackToHistory by lazy { Creator.provideAddTrackToHistoryUseCase(getApplication()) }
    private val clearHistory by lazy { Creator.provideClearHistoryUseCase(getApplication()) }
    private val getTracksHistory by lazy { Creator.provideGetTrackHistoryFromStorageUseCase(getApplication()) }
    private val checkIsHistoryEmpty by lazy { Creator.provideCheckIsHistoryEmptyUseCase(getApplication()) }

    init {
        renderHistoryState(
            HistoryTrackListState.Loading
        )
        val getTracksHistory = getTracksHistory()
        if (getTracksHistory.isNullOrEmpty()) {
            renderHistoryState(HistoryTrackListState.Empty("Список пуст"))
        } else {
            renderHistoryState(HistoryTrackListState.Content(getTracksHistory))
        }

    }

    private val gson = GsonProvider.gson
    private val handler = Handler(Looper.getMainLooper())

    private val trackList = mutableListOf<Track>()
    private var latestSearchedText: String? = null

    private val stateLiveDate = MutableLiveData<TrackListState>()
    private val historyStateLiveData = MutableLiveData<HistoryTrackListState>()


    private val mediatorStateLiveData = MediatorLiveData<TrackListState>().also { livedata ->
        livedata.addSource(stateLiveDate) { trackListState ->
            livedata.value = when (trackListState) {
                is TrackListState.Loading -> trackListState
                is TrackListState.Content -> TrackListState.Content(trackListState.tracks)
                is TrackListState.Error -> trackListState
                is TrackListState.NoConnection -> trackListState
                is TrackListState.Empty -> trackListState
            }
        }

//        livedata.addSource(historyStateLiveData){ historyTrackListState ->
//            livedata.value = when (historyTrackListState) {
//                is HistoryTrackListState.Loading -> historyTrackListState
//                is HistoryTrackListState.Content -> HistoryTrackListState.Content(historyTrackListState.tracks)
//                is HistoryTrackListState.Empty -> historyTrackListState
//            }
//
//        }
    }

    fun observeHistoryState(): LiveData<HistoryTrackListState> = historyStateLiveData
    fun observeState(): LiveData<TrackListState> = mediatorStateLiveData

    fun clearHistoryList() {
        clearHistory()
        updateHistoryList()
    }

    fun clearSearchList() {
        renderState(
            TrackListState.Content(
                emptyList<Track>()
            )
        )
    }
    fun addTrackToHistoryList(track: Track) {
        addTrackToHistory(track = track)
        updateHistoryList()
    }

    private fun updateHistoryList() {
        val currentState = historyStateLiveData.value
        if (currentState is HistoryTrackListState.Content) {
            historyStateLiveData.value = HistoryTrackListState.Content(getTracksHistory())
        }
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
                TrackListState.Loading
            )
        }
        getTrackList(
            expression = query,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data) {
                        is ConsumerData.Error -> renderState(TrackListState.Error(data.message))
                        is ConsumerData.NoConnection -> renderState(TrackListState.NoConnection(data.message))
                        is ConsumerData.Data -> {
                            if (data.value.isNullOrEmpty()) {
                                renderState(TrackListState.Empty("По запросу ничего не нашлось"))
                            } else {
                                renderState(TrackListState.Content(data.value))
                            }
                        }
                    }
                }
            }
        )

    }

//    getTrackList(
//    expression = binding.editTextwather.text.toString(),
//    consumer = object : Consumer<List<Track>> {
//        override fun consume(data: ConsumerData<List<Track>>) {
//            val currentRunnable = searchRunnable
//            if (currentRunnable != null) {
//                handler.removeCallbacks(currentRunnable)
//            }
//            val newSearchRunnable = Runnable {
//                binding.progressBar?.visibility = View.GONE
//                when (data) {
//                    is ConsumerData.Error -> showError(data.message)
//                    is ConsumerData.NoConnection -> showErrorBadConnection()
//                    is ConsumerData.Data -> {
//                        if (data.value.isNullOrEmpty()) {
//                            showErrorNothingFound(binding.editTextwather.text.toString())
//                        } else {
//                            binding.recyclerSearch?.visibility = View.VISIBLE
//                            binding.trackHistory?.visibility = View.GONE
//                            binding.nothingFoundPlaceHolder.visibility = View.GONE
//                            binding.badConnectionPlaceHolder.visibility = View.GONE
//                            trackList.clear()
//                            trackList.addAll(data.value!!)
//                            adapter.updateItems(trackList)
//                        }
//                    }
//
//
//                }
//            }
//            handler.post(newSearchRunnable)
//        }
//    }
//    )


    private fun renderState(state: TrackListState) {
        stateLiveDate.postValue(state)
    }

    private fun renderHistoryState(historyState: HistoryTrackListState) {
        historyStateLiveData.postValue(historyState)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getSearchViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }

        private const val KEY = "KEY"
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }
}