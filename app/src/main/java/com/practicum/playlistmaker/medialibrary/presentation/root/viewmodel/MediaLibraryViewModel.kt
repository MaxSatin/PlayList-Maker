package com.practicum.playlistmaker.medialibrary.presentation.root.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.screen_state.FavoriteListScreenState
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.state.State

import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import com.practicum.playlistmaker.medialibrary.domain.screen_state.PlayListsScreenState
import com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.utils.SingleLineEvent
import kotlinx.coroutines.launch

class MediaLibraryViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor
) : ViewModel() {

    private val mediaLibraryStateLiveData = MutableLiveData<State>()
    fun getMediaLibraryStateLiveData(): LiveData<State> = mediaLibraryStateLiveData

    private val showToast = SingleLineEvent<String>()
    fun observeToast(): LiveData<String> = showToast

    init {
        loadFavoriteTrackList()
    }

    private fun loadFavoriteTrackList() {
        mediaLibraryStateLiveData.value = getCurrentMediaLibraryState().copy(
            favoriteListScreenState = FavoriteListScreenState.Loading
        )

        viewModelScope.launch {
            mediaLibraryInteractor.getFavoriteTrackList()
                .collect { trackList ->
                    processResult(trackList)
                }
        }
    }

    private fun processResult(trackList: List<Track>) {
        if (trackList.isEmpty()) {
            mediaLibraryStateLiveData.value = getCurrentMediaLibraryState().copy(
                favoriteListScreenState = FavoriteListScreenState.Empty("Список треков пуст!")
            )
        } else {
            mediaLibraryStateLiveData.value = getCurrentMediaLibraryState().copy(
                favoriteListScreenState = FavoriteListScreenState.Content(trackList)
            )
        }
    }

    private fun getCurrentMediaLibraryState(): State {
        return mediaLibraryStateLiveData.value ?: State(
            favoriteListScreenState = FavoriteListScreenState.Empty("Список треков пуст!"),
            PlayListsScreenState.Empty("Список плейлистов пуст!")
        )
    }
}



