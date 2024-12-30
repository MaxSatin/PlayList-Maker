package com.practicum.playlistmaker.medialibrary.presentation.utils

import androidx.lifecycle.MediatorLiveData
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.PlayListsScreenState

class LiveData<T: PlayListsScreenState> : MediatorLiveData<T>() {
    private var lastValue: List<Playlist>? = null

    override fun setValue(value: T) {
        when (value){
            is PlayListsScreenState.Content ->  if (!areListsEqual(lastValue, value.playlists)) {
                lastValue = value.playlists
                super.setValue(value)
            }
        }
        super.setValue(value)
    }

    private fun areListsEqual(oldList: List<T>?, newList: List<T>?): Boolean {
        return oldList == newList || (oldList != null && newList != null && oldList.size == newList.size && oldList.containsAll(newList))
    }
}