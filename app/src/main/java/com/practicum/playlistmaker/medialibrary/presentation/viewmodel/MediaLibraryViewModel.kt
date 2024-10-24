package com.practicum.playlistmaker.medialibrary.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor

import com.practicum.playlistmaker.medialibrary.domain.track_model.State

class MediaLibraryViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor
) : ViewModel() {

    private val mediaLibraryStateLiveData = MutableLiveData<State>()
    fun getMediaLibraryStateLiveData(): LiveData<State> = mediaLibraryStateLiveData

    init{
        showContent()
    }

    private fun getCurrentMediaLibraryState(): State {
        return mediaLibraryStateLiveData.value ?: State(
            favoriteTracks = emptyList(),
            playList = emptyList()
        )
    }

    private fun showContent() {
        mediaLibraryStateLiveData.value = getCurrentMediaLibraryState().copy(
            favoriteTracks = mediaLibraryInteractor.getFavoriteTrackList(),
            playList = mediaLibraryInteractor.getPlaylists()
        )
    }
}