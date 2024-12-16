package com.practicum.playlistmaker.medialibrary.presentation.playlists.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.CreatePlaylistPermissionState
import com.practicum.playlistmaker.search.presentation.utils.debounce
import kotlinx.coroutines.launch


class CreatePlayListsViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
) : ViewModel() {

    private var isCopyNeeded = false

    private val permissionState = MutableLiveData<CreatePlaylistPermissionState>()
    fun permissionStateLiveData(): LiveData<CreatePlaylistPermissionState> = permissionState

    private var isClickAllowed = true
    private val setIsClickAllowed = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        viewModelScope,
        false
    ) { isAllowed ->
        isClickAllowed = isAllowed
    }

    init {
        isCopyNeeded = false
    }

    fun createPlaylists(playlist: Playlist) {
        if (clickDebounce()) {
            viewModelScope.launch {
                mediaLibraryInteractor.getPlaylists()
                    .collect { playlists ->
                        val filteredPlaylist = playlists.filter { it.name == playlist.name }
                        if (filteredPlaylist.isNotEmpty()) {
                            renderState(
                                CreatePlaylistPermissionState.NeededPermission
                            )
                        }
                        if (isCopyNeeded) {
                            addPlaylist(playlist)
                        } else {
                            addPlaylistWithReplace(playlist)
                        }
                    }
            }
        }
    }

    private fun onPermissionGranted() {
        isCopyNeeded = true
    }

    private fun onPermissionDenied() {
        isCopyNeeded = false
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            setIsClickAllowed(true)
        }
        return current
    }

    private fun renderState(state: CreatePlaylistPermissionState) {
        permissionState.postValue(state)
    }


    private fun addPlaylistWithReplace(playlist: Playlist) {
        viewModelScope.launch {
            mediaLibraryInteractor.addPlaylistWithReplace(playlist)
        }
    }

    private fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            mediaLibraryInteractor.addPlaylist(playlist)
        }
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}

