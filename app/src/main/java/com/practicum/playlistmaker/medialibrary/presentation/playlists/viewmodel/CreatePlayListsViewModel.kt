package com.practicum.playlistmaker.medialibrary.presentation.playlists.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.CreatePlaylistState
import com.practicum.playlistmaker.search.presentation.utils.debounce
import kotlinx.coroutines.launch


class CreatePlayListsViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
) : ViewModel() {

    private var isCopyNeeded = false


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
    private val checkPlaylistExists = debounce<Playlist>(
        CLICK_DEBOUNCE_DELAY,
        viewModelScope,
        false
    ) { playlist ->
//        checkCurrentPlaylists(playlist)
    }

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
//                    var playlistst = playlists
//                    val filteredPlaylist = playlistst.find {
//                        it.name.trim().equals(playlist.name.trim(), ignoreCase = true)
//                    }
//                    Log.d("ViewmodelPlaylistR", "$filteredPlaylist")
//                    if (filteredPlaylist?.name.isNullOrEmpty()) {
//                        renderState(
//                            CreatePlaylistState.NoCopyExists
//                        )
//                    }
//                    renderState(
//                        CreatePlaylistState.CopyExists
//                    )
                }
//                        if (isCopyNeeded) {
//                            addPlaylist(playlist)
//                        } else {
//                            addPlaylistWithReplace(playlist)
//                        }
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

