package com.practicum.playlistmaker.medialibrary.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.track_model.State
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track

class PlayListsViewModel(): ViewModel()
//    private val mediaLibraryInteractor: MediaLibraryInteractor
//): ViewModel() {
//
//    private var playListsLiveData = MutableLiveData<State>()
//    fun getPlayListsLiveData(): LiveData<State> = playListsLiveData
//
//    init {
//        showContent()
//    }
//    private fun getCurrentState(): State {
//        return playListsLiveData.value ?: State (
//            playList = emptyList<List<Track>>()
//        )
//    }
//
//    private fun showContent(){
//        playListsLiveData.value = getCurrentState().copy(
//            playList = mediaLibraryInteractor.getPlaylists()
//        )
//    }
//}