package com.practicum.playlistmaker.medialibrary.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track

class FavoriteTracksViewModel(): ViewModel()
//    private val mediaLibraryInteractor: MediaLibraryInteractor
//) : ViewModel() {
//
//    private val favoriteTrackListLiveData = MutableLiveData<FavoriteTrackListState>()
//    fun getFavoriteTrackListLiveData(): LiveData<FavoriteTrackListState> = favoriteTrackListLiveData
//
//    init{
////        showLoading()
//        showContent()
//    }
//
//    private fun getCurrentMediaLibraryState(): FavoriteTrackListState {
//        return favoriteTrackListLiveData.value ?: FavoriteTrackListState(
//            favoriteTracks = emptyList(),
//        )
//    }
//
////    private fun showLoading() {
////        favoriteTrackListLiveData.value = getCurrentMediaLibraryState().copy(
////            isLoading = true
////        )
////    }
//
//    private fun showContent() {
//        favoriteTrackListLiveData.value = getCurrentMediaLibraryState().copy(
//            favoriteTracks = mediaLibraryInteractor.getFavoriteTrackList(),
//        )
//    }
//}

