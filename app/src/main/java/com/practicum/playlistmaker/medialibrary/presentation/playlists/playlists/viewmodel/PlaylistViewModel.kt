package com.practicum.playlistmaker.medialibrary.presentation.playlists.playlists.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.PlayListsScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
) : ViewModel() {

    private val playlistScreenStateLiveData = MutableLiveData<PlayListsScreenState>()
    fun getPlayListScreenState(): LiveData<PlayListsScreenState> = playlistScreenStateLiveData

    fun getPlaylists() {

        viewModelScope.launch {
            val playlists = mutableListOf<Playlist>()
            withContext(Dispatchers.IO) {
                mediaLibraryInteractor.getPlaylists()
                    .flatMapConcat { playlists ->
                        playlists.asFlow()
                    }.map { playlist ->
                        mediaLibraryInteractor.getAllTracksFromPlaylist(playlist.name)
                            .map { tracksNumber ->
                                playlist.copy(tracksNumber = tracksNumber.size)
                            }
                            .collect { updatedPlaylists ->
                                playlists.add(updatedPlaylists)
                            }

                    }

            }
            processResult(playlists)
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