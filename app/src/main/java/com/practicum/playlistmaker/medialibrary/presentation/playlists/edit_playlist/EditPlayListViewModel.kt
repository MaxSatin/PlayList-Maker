package com.practicum.playlistmaker.medialibrary.presentation.playlists.edit_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.create_playlist.CreatePlaylistState
import com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details.PlaylistState
import com.practicum.playlistmaker.search.presentation.utils.debounce
import kotlinx.coroutines.launch

class EditPlayListViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
) : ViewModel() {

    private var isCopyNeeded = false

    private val playlistDetailsLiveData = MutableLiveData<PlaylistState>()
    fun getPlaylistDetailsLiveData(): LiveData<PlaylistState> = playlistDetailsLiveData

    private val playlistsState = MutableLiveData<CreatePlaylistState>()
    fun permissionStateLiveData(): LiveData<CreatePlaylistState> = playlistsState

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

    fun checkPlaylistDebounce(playlist: Playlist) {
        if (latestCheckedPlayList == playlist) {
            return
        }
        this.latestCheckedPlayList = playlist
        checkPlaylistDebounce(playlist)
    }

    fun checkCurrentPlaylists() {
        viewModelScope.launch {
            mediaLibraryInteractor.getPlaylists()
                .collect { playlists ->
                    renderState(CreatePlaylistState.Content(playlists))
                }
        }
    }

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
                PlaylistState.Empty("Плейлист не найден!")
            )
        } else {
            renderPlaylistState(
                PlaylistState.DetailsState(playList)
            )
        }
    }

    private fun renderPlaylistState(state: PlaylistState) {
        playlistDetailsLiveData.postValue(state)
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            setIsClickAllowed(true)
        }
        return current
    }

    private fun renderState(state: CreatePlaylistState) {
        playlistsState.postValue(state)
    }


    fun addPlaylistWithReplace(playlist: Playlist) {
        viewModelScope.launch {
            mediaLibraryInteractor.addPlaylistWithReplace(playlist)
        }
    }

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            mediaLibraryInteractor.addPlaylist(playlist)
        }
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}