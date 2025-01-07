package com.practicum.playlistmaker.medialibrary.presentation.playlists.playlist_details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.domain.interactor.MediaLibraryInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details.NavigateFragment
import com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details.PlaylistDetailsScreenState
import com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details.PlaylistState
import com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details.TrackListState
import com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.utils.SingleEventLifeData
import com.practicum.playlistmaker.medialibrary.presentation.favorite_tracks.utils.debounce
import com.practicum.playlistmaker.medialibrary.ui.edit_playlist_fragment.EditPlayListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistDetailsViewModel(
    private val mediaLibraryInteractor: MediaLibraryInteractor,
    private val gson: Gson,
) : ViewModel() {

    private val playlistDetailsLiveData = MutableLiveData<PlaylistState>()
    fun getPlaylistDetailsLiveData(): LiveData<PlaylistState> = playlistDetailsLiveData

    private val trackListLiveData = MutableLiveData<TrackListState>()
    fun getTrackListStateLiveData(): LiveData<TrackListState> = trackListLiveData

    private val showFragmentLiveData = SingleEventLifeData<NavigateFragment>()
    fun getShowFragmentLiveData(): LiveData<NavigateFragment> = showFragmentLiveData

    private val playListMediatorLiveData = MediatorLiveData<PlaylistDetailsScreenState>().apply {
        var overallDuration: Long = 0L
        var playlist: Playlist? = null
        var trackList: List<Track>? = null

        fun updateState() {
            if (playlist != null && trackList != null) {
                postValue(
                    PlaylistDetailsScreenState.DetailsState(
                        overallDuration,
                        playlist,
                        trackList!!
                    )
                )
            } else {
                postValue(PlaylistDetailsScreenState.Loading)
            }
        }

        addSource(playlistDetailsLiveData) { playListState ->
            playlist = when (playListState) {
                is PlaylistState.DetailsState -> playListState.playlist
                is PlaylistState.Empty -> null
            }
            updateState()
        }

        addSource(trackListLiveData) { trackListState ->
            when (trackListState) {
                is TrackListState.Contents -> {
                    viewModelScope.launch {
                        overallDuration = provideOverallTrackLength(trackListState.trackList)
                        trackList = trackListState.trackList
                        updateState()
                    }
                }

                is TrackListState.Empty -> {
                    trackList = emptyList()
                    updateState()
                }
            }
        }
    }


    fun getPlayListDetailsMediatorLiveData(): LiveData<PlaylistDetailsScreenState> =
        playListMediatorLiveData
//    private val playListMediatorLiveData =
//        MediatorLiveData<PlaylistDetailsScreenState>().also { liveData ->
//
//            var overallDuration: Long = 0L
//            var playlist: Playlist? = null
//            var trackList: List<Track>? = null
//            liveData.addSource(playlistDetailsLiveData) { playListState ->
//                playlist = when (playListState) {
//                    is PlaylistState.DetailsState -> playListState.playlist
//                    is PlaylistState.Empty -> null
//                }
//            }
//            liveData.addSource(trackListLiveData) { trackListState ->
//                when (trackListState) {
//                    is TrackListState.Contents -> {
//                        viewModelScope.launch {
//                            overallDuration = provideOverallTrackLength(trackListState.trackList)
//                        }
//                        trackList = trackListState.trackList
//                    }
//                    is TrackListState.Empty -> trackList = emptyList()
//                }
//            }
//
//            liveData.postValue(PlaylistDetailsScreenState.Loading)
//
//            if (playlist != null && trackList != null) {
//                liveData.postValue(
//                    PlaylistDetailsScreenState.DetailsState(
//                        overallDuration,
//                        playlist,
//                        trackList
//                    )
//                )
//            }
//        }

    private var isClickAllowed: Boolean = true

    private val setClickAllowed = debounce<Boolean>(
        CLICK_DELAY_MILLIS,
        viewModelScope,
        false
    ) { isAllowed ->
        isClickAllowed = isAllowed
    }

    fun loadPlayListDetails(playlistName: String){
        viewModelScope.launch {
            val playList = mediaLibraryInteractor.getPlaylistByName(playlistName)
            processPlayListResult(playList)

            mediaLibraryInteractor.getAllTracksFromPlaylist(playlistName)
                .collect { trackList ->
                    processTrackListResult(trackList)
                }
        }
    }

   private fun getAllTracksFromPlaylist(playlistName: String) {
        viewModelScope.launch {
            mediaLibraryInteractor.getAllTracksFromPlaylist(playlistName)
                .collect { trackList ->
                    processTrackListResult(trackList)
                }
        }
    }

    private fun loadPlaylistDetailsState(playlistName: String) {
        viewModelScope.launch {
            val playList = mediaLibraryInteractor.getPlaylistByName(playlistName)
            processPlayListResult(playList)
        }
    }

//    fun loadPlaylistDetails(playlistName: String) {
//        viewModelScope.launch {
//            val playlist = mediaLibraryInteractor.getPlaylistByName(playlistName)
//            renderPlaylistState(
//                getCurrentPlaylistDetailsScreenState().copy(
//                    playlist = playlist,
//                    emptyMessage = ""
//                )
//            )
//        }
//    }

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
            showFragmentLiveData.postValue(
                NavigateFragment.PlayerFragment(trackGson)
            )
        }
    }

    fun showEditPlayListFragment(playListName: String){
        if(clickDebounce()){
            showFragmentLiveData.postValue(
                NavigateFragment.EditPlayListFragment(playListName)
            )
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

//    private fun getCurrentPlaylistDetailsScreenState(): PlaylistDetailsScreenState {
//        return playlistDetailsLiveData.value ?: PlaylistDetailsScreenState(
//            0L,
//            null,
//            emptyList(),
//            "Данные о треках и плейлисте отсутствуют"
//        )
//    }

    private fun renderPlaylistState(state: PlaylistState) {
        playlistDetailsLiveData.postValue(state)
    }

    private fun renderTrackListState(state: TrackListState) {
        trackListLiveData.postValue(state)
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


    private fun processTrackListResult(trackList: List<Track>) {
        if (trackList.isEmpty()) {
            renderTrackListState(
                TrackListState.Empty("Список треков пуст!")
//                getCurrentPlaylistDetailsScreenState().copy(
//                    contents = emptyList(),
//                    emptyMessage = "Список плейлистов пуст!"
//                )
            )
        } else {
//            viewModelScope.launch {
//                val playlistDuration = provideOverallTrackLength(trackList)
            renderTrackListState(
                TrackListState.Contents(
//                        overallDuration = playlistDuration,
                    trackList = trackList,
                )
//                    getCurrentPlaylistDetailsScreenState().copy(
//                        isLoading = false,
//                        overallDuration = playlistDuration,
//                        contents = trackList,
//                        emptyMessage = ""
//                    )
            )
        }

    }


    private companion object {

        private const val CLICK_DELAY_MILLIS = 1_500L
    }
}


