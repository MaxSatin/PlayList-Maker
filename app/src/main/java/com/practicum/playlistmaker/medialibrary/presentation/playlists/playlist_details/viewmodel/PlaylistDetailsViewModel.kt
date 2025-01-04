package com.practicum.playlistmaker.medialibrary.presentation.playlists.playlist_details.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.screen_state.PlaylistDetailsScreenState
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
) : ViewModel() {

    private val playlistDetailsLiveData= MutableLiveData<PlaylistDetailsScreenState>()
    fun getPlaylistDetailsLiveData() = playlistDetailsLiveData

    fun getAllTracksFromPlaylist(playlistName: String){
        viewModelScope.launch {
            mediaLibraryInteractor.getAllTracksFromPlaylist(playlistName)
                .collect{ playlistDetailsState ->

                }
        }

    }

    private fun renderState(state: PlaylistDetailsScreenState){
        playlistDetailsLiveData.postValue(state)
    }

}