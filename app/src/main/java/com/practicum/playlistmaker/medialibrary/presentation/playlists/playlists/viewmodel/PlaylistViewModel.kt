package com.practicum.playlistmaker.medialibrary.presentation.playlists.playlists.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.PlayListsScreenState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
) : ViewModel() {

    private val playlistScreenStateLiveData = MutableLiveData<PlayListsScreenState>()
    fun getPlayListScreenState(): LiveData<PlayListsScreenState> = playlistScreenStateLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            mediaLibraryInteractor.getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun render(state: PlayListsScreenState) {
        playlistScreenStateLiveData.postValue(state)

    }

    private fun processResult(playLists: List<Playlist>) {
        if (playLists.isEmpty()) {
            render(
                PlayListsScreenState.Empty("Список плейлистов пуст!")
            )
        } else {
            render(
                PlayListsScreenState.Content(playLists)
            )
        }
    }
}