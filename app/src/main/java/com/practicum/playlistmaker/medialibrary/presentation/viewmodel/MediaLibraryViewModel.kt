package com.practicum.playlistmaker.medialibrary.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.screen_state.FavoriteListScreenState
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.track_model.State

import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import com.practicum.playlistmaker.medialibrary.presentation.utils.SingleLineEvent
import kotlinx.coroutines.launch

class MediaLibraryViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor
) : ViewModel() {

//    private val favoriteScreenState = MutableLiveData<FavoriteListScreenState>()
//    fun getFavoriteListScreenState(): LiveData<FavoriteListScreenState> = favoriteScreenState

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
//        render(
//            FavoriteListScreenState.Loading
//        )

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
//            render(FavoriteListScreenState.Empty("Список треков пуст!"))
        } else {
            mediaLibraryStateLiveData.value = getCurrentMediaLibraryState().copy(
                favoriteListScreenState = FavoriteListScreenState.Content(trackList)
            )
//            render(FavoriteListScreenState.Content(trackList))
        }
    }
//    private fun render(state: FavoriteListScreenState){
//        favoriteScreenState.postValue(state)
//    }


//    private val mediaLibraryStateLiveData = MutableLiveData<State>()
//    fun getMediaLibraryStateLiveData(): LiveData<State> = mediaLibraryStateLiveData

//    init{
//        showContent()
//    }

    private fun getCurrentMediaLibraryState(): State {
        return mediaLibraryStateLiveData.value ?: State(
            favoriteListScreenState = FavoriteListScreenState.Empty("Список треков пуст!"),
            playList = emptyList()
        )
    }
}


//    private fun showContent() {
//        mediaLibraryStateLiveData.value = getCurrentMediaLibraryState().copy(
//            favoriteTracks = mediaLibraryInteractor.getFavoriteTrackList(),
//            playList = mediaLibraryInteractor.getPlaylists()
//        )


