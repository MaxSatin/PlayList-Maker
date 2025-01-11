package com.practicum.playlistmaker.medialibrary.presentation.playlists.edit_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.edit_playlist_data_state.EditPlaylistDataState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.createplaylists.viewmodel.CreatePlayListsViewModel

class EditPlaylistDataViewModel(
    mediaLibraryInteractor: MediaLibraryInteractor,
    private val gson: Gson
): CreatePlayListsViewModel(mediaLibraryInteractor) {

    private val playlistEditInfoLiveData = MutableLiveData<EditPlaylistDataState>()
    fun getPlayListInfoLiveData(): LiveData<EditPlaylistDataState> = playlistEditInfoLiveData

    fun initializePlaylistData(playlistGson: String){
        val playlist = gson.fromJson<Playlist>(playlistGson, Playlist::class.java)
        playlistEditInfoLiveData.postValue(
            EditPlaylistDataState.Content(
                playlist,
                "Редактировать",
                "Сохранить"
            )
        )
    }
}