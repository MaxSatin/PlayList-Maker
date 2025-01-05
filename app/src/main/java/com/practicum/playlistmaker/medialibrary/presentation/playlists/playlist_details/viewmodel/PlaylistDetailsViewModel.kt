package com.practicum.playlistmaker.medialibrary.presentation.playlists.playlist_details.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import com.practicum.playlistmaker.medialibrary.domain.screen_state.PlaylistDetailsScreenState
import com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.utils.SingleEventLifeData
import com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.utils.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistDetailsViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
    private val gson: Gson,
) : ViewModel() {

    private val playlistDetailsLiveData = MutableLiveData<PlaylistDetailsScreenState>()
    fun getPlaylistDetailsLiveData(): LiveData<PlaylistDetailsScreenState> = playlistDetailsLiveData

    private val showPlayerLiveData = SingleEventLifeData<String>()
    fun getShowPlayerLiveData(): LiveData<String> = showPlayerLiveData

    private var isClickAllowed: Boolean = true

    private val setClickAllowed = debounce<Boolean>(
        CLICK_DELAY_MILLIS,
        viewModelScope,
        false
    ) { isAllowed ->
        isClickAllowed = isAllowed
    }


    fun getAllTracksFromPlaylist(playlistName: String) {
        viewModelScope.launch {
            mediaLibraryInteractor.getAllTracksFromPlaylist(playlistName)
                .collect { trackList ->
                    processTrackListResult(trackList)
                }
        }
    }

    fun loadPlaylistDetails(playlistName: String) {
        viewModelScope.launch {
            val playlist = mediaLibraryInteractor.getPlaylistByName(playlistName)
            renderState(
                getCurrentPlaylistDetailsScreenState().copy(
                    playlist = playlist,
                    emptyMessage = ""
                )
            )
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            setClickAllowed(true)
        }
        return current
    }

    private suspend fun provideOverallTrackLength(trackList: List<Track>): Long {
        return withContext(Dispatchers.Default) {
            var trackLength = 0L
            trackList.forEach { trackLength += it.trackTimeMillis }
            trackLength / 60_000
        }
    }

    fun showTrackPlayer(track: Track) {
        if (clickDebounce()) {
            val trackGson = gson.toJson(track)
            showPlayerLiveData.postValue(trackGson)
        }
    }

    fun updatePlayList(oldName: String, newName: String) {
        viewModelScope.launch {
            mediaLibraryInteractor.updatePlaylist(oldName, newName)
        }
    }

    fun deleteTrackFromPlaylist(playListName: String, trackId: String) {
        viewModelScope.launch {
            mediaLibraryInteractor.deleteTrackFromPlaylist(playListName, trackId)
        }
    }

    private fun getCurrentPlaylistDetailsScreenState(): PlaylistDetailsScreenState {
        return playlistDetailsLiveData.value ?: PlaylistDetailsScreenState(
            false,
            0L,
            null,
            emptyList(),
            "Данные о треках и плейлисте отсутствуют"
        )
    }

    private fun renderState(state: PlaylistDetailsScreenState) {
        playlistDetailsLiveData.postValue(state)
    }

    private fun processTrackListResult(trackList: List<Track>) {
        if (trackList.isEmpty()) {
            renderState(
                getCurrentPlaylistDetailsScreenState().copy(
                    contents = emptyList(),
                    emptyMessage = "Список плейлистов пуст!"
                )
            )
        } else {
            viewModelScope.launch {
                val playlistDuration = provideOverallTrackLength(trackList)
                renderState(
                    getCurrentPlaylistDetailsScreenState().copy(
                        overallDuration = playlistDuration,
                        contents = trackList,
                        emptyMessage = ""
                    )
                )
            }

        }
    }

    private companion object {
        private const val CLICK_DELAY_MILLIS = 1_500L
    }
}


