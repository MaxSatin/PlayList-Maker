package com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.domain.screen_state.media_library.FavoriteListScreenState
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.utils.SingleLineEvent
import com.practicum.playlistmaker.search.presentation.utils.debounce
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
    private val gson: Gson,
) : ViewModel() {

    private val favoriteScreenState = MutableLiveData<FavoriteListScreenState>()
    fun getFavoriteListScreenState(): LiveData<FavoriteListScreenState> = favoriteScreenState

    private val showToast = SingleLineEvent<String>()
    fun observeToast(): LiveData<String> = showToast

    private val showTrackPlayerTrigger = SingleLineEvent<String>()
    fun getShowTrackPlayerTrigger(): LiveData<String> = showTrackPlayerTrigger

    private var isClickAllowed = true
    private val setIsClickAllowed = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        viewModelScope,
        false
    ) { isAllowed ->
        isClickAllowed = isAllowed
    }


    fun loadFavoriteTrackList() {
//        render(
//            FavoriteListScreenState.Loading
//        )

        viewModelScope.launch {
            mediaLibraryInteractor.getFavoriteTrackList()
                .collect { trackList ->
                    processResult(trackList)
                    Log.d("FavoriteList", "${trackList}")
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
            val trackGson = gson.toJson(track)
            showTrackPlayerTrigger.value = trackGson
        }
    }

    private fun processResult(trackList: List<Track>) {
        if (trackList.isEmpty()) {
            render(FavoriteListScreenState.Empty("Список треков пуст!"))
        } else {
            render(FavoriteListScreenState.Content(trackList))
        }
    }

    private fun render(state: FavoriteListScreenState) {
        favoriteScreenState.postValue(state)
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }

}

