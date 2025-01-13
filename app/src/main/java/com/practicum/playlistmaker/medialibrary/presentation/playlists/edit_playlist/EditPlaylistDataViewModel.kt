package com.practicum.playlistmaker.medialibrary.presentation.playlists.edit_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.create_playlist.CreatePlaylistState
import com.practicum.playlistmaker.medialibrary.domain.screen_state.edit_playlist_data_state.EditPlaylistDataState
import com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details.PlaylistState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.createplaylists.viewmodel.CreatePlayListsViewModel
import com.practicum.playlistmaker.search.presentation.utils.debounce
import kotlinx.coroutines.launch

class EditPlaylistDataViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
    private val gson: Gson,
) : CreatePlayListsViewModel(mediaLibraryInteractor) {

//    private val playlistEditInfoLiveData = MutableLiveData<EditPlaylistDataState>()
//    fun getPlayListInfoLiveData(): LiveData<EditPlaylistDataState> = playlistEditInfoLiveData
//
//    fun initializePlaylistData(playlistGson: String){
//        val playlist = gson.fromJson<Playlist>(playlistGson, Playlist::class.java)
//        playlistEditInfoLiveData.postValue(
//            EditPlaylistDataState.Content(
//                playlist,
//                "Редактировать",
//                "Сохранить"
//            )
//        )
//    }

    private var isCopyNeeded = false

    private val playlistDetailsLiveData = MutableLiveData<EditPlaylistDataState>()
    fun getPlaylistDetailsLiveData(): LiveData<EditPlaylistDataState> = playlistDetailsLiveData

    private val playlistsState = MutableLiveData<CreatePlaylistState>()
//    fun permissionStateLiveData(): LiveData<CreatePlaylistState> = playlistsState

    private var isClickAllowed = true
    private val setIsClickAllowed = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        viewModelScope,
        false
    ) { isAllowed ->
        isClickAllowed = isAllowed
    }

    private var latestCheckedPlayList: Playlist? = null

    init {
        isCopyNeeded = false
    }

//    fun checkPlaylistDebounce(playlist: Playlist) {
//        if (latestCheckedPlayList == playlist) {
//            return
//        }
//        this.latestCheckedPlayList = playlist
//        checkPlaylistDebounce(playlist)
//    }

//    fun checkCurrentPlaylists() {
//        viewModelScope.launch {
//            mediaLibraryInteractor.getPlaylists()
//                .collect { playlists ->
//                    renderState(CreatePlaylistState.Content(playlists))
//                }
//        }
//    }

    fun loadPlaylistDetailsState(playlistId: Long) {
        viewModelScope.launch {
            mediaLibraryInteractor.getPlaylistById(playlistId)
                .collect { playlist ->
                    processPlayListResult(playlist)
                }
        }
    }

    fun updatePlaylist(
        playlistId: Long, newPlaylistName: String,
        newDescription: String, newCoverUri: String,
    ) {
        viewModelScope.launch {
            mediaLibraryInteractor.updatePlaylistTable(
                playlistId, newPlaylistName,
                newDescription, newCoverUri
            )
        }
    }

    private fun processPlayListResult(playList: Playlist) {
        if (playList.name.isEmpty()) {
            renderPlaylistState(
                EditPlaylistDataState.Empty("Плейлист не найден!")
            )
        } else {
            renderPlaylistState(
                EditPlaylistDataState.Content(
                    playList,
                    "Редактировать",
                    "Сохранить"
                )
            )
        }
    }

    private fun renderPlaylistState(state: EditPlaylistDataState) {
        playlistDetailsLiveData.postValue(state)
    }


    private fun renderState(state: CreatePlaylistState) {
        playlistsState.postValue(state)
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}