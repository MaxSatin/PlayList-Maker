package com.practicum.playlistmaker.medialibrary.presentation.playlists.playlists.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.media_library.PlayListsScreenState
import com.practicum.playlistmaker.medialibrary.presentation.utils.SingleLineEvent
import kotlinx.coroutines.launch


class PlaylistViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
) : ViewModel() {

    private var lastPostedList = emptyList<Playlist>()

    private val playlistScreenStateLiveData = MutableLiveData<PlayListsScreenState>()
    fun getPlayListScreenState(): LiveData<PlayListsScreenState> = playlistScreenStateLiveData

    private val mediatorStateLiveData = SingleLineEvent<PlayListsScreenState>().also { livedata ->
        livedata.addSource(playlistScreenStateLiveData) { playlistState ->
            when (playlistState) {
                is PlayListsScreenState.Content -> {
                    if (!areListsEqual(lastPostedList, playlistState.playlists)) {
                        lastPostedList = playlistState.playlists
                        livedata.postValue(
                            PlayListsScreenState.Content(
                                playlistState.playlists
                            )
                        )
                    }
                }

                else -> livedata.postValue(
                    PlayListsScreenState.Empty("Список плейлистов пуст!")
                )
            }
        }
    }

    fun playlistStateMediatorLiveData(): LiveData<PlayListsScreenState> = mediatorStateLiveData

    private fun areListsEqual(oldList: List<Playlist>?, newList: List<Playlist>?): Boolean {
        return oldList == newList || (oldList != null && newList != null && oldList.size == newList.size && oldList.containsAll(
            newList
        ))
    }

    fun getPlaylists() {
        viewModelScope.launch {
            mediaLibraryInteractor.getPlaylists()
                .collect { playLists ->
                    Log.d("PlaylistsFragment", "$playLists")
                    processResult(playLists)
                }
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