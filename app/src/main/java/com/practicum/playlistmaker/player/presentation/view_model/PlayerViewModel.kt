package com.practicum.playlistmaker.player.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.player.domain.db_interactor.DatabaseInteractor
import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.player.domain.player_interactor.MediaPlayerInteractor
import com.practicum.playlistmaker.player.presentation.mapper.DateFormatter
import com.practicum.playlistmaker.player.presentation.mapper.TrackInfoMapper
import com.practicum.playlistmaker.player.domain.model.track_model.Track
import com.practicum.playlistmaker.player.presentation.state.PlayListsScreenState
import com.practicum.playlistmaker.player.presentation.state.PlayStatus
import com.practicum.playlistmaker.player.presentation.state.PlayerState
import com.practicum.playlistmaker.player.presentation.state.TrackState
import com.practicum.playlistmaker.search.presentation.utils.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel(
    application: Application,
    trackGson: String?,
    private val databaseInteractor: DatabaseInteractor,
    private val playerInteractor: MediaPlayerInteractor,
    private val gson: Gson,
) : AndroidViewModel(application) {
    private val trackItem = loadTrackScreen(trackGson)


    private var timerJob: Job? = null

    private var isClickAllowed: Boolean = true

    private val setIsClickAllowed = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        viewModelScope,
        false
    ) { isAllowed ->
        isClickAllowed = isAllowed
    }

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private val playlistStateLiveData = MutableLiveData<PlayListsScreenState>()
    fun getPlaylistStateLiveData() = playlistStateLiveData

    private val checkTrackBelongsToPlaylistLiveData = MutableLiveData<TrackState>()
    fun getCheckTrackBelongsToPlaylistLiveData() = checkTrackBelongsToPlaylistLiveData

    init {
        showLoading()
        loadContent()
        preparePlayer()
        getPlaylists()
    }

    fun refreshPlayerState() {
        playerStateLiveData.value = getCurrentPlayerState()
    }

    fun resetPlayer() {
        playerInteractor.resetPlayer()
    }

    fun isPlayerPrepared(): Boolean{
        return playerInteractor.isPlayerPrepared()
    }

//    fun setTrackGson(trackGson: String?){
//        if (!trackGson.isNullOrEmpty())
//        this.trackGson = trackGson
//    }

    fun getPlaylists() {
        viewModelScope.launch {
            databaseInteractor.getPlaylistsWithTrackCount()
                .collect { playlist ->
                    processResult(playlist)
                }
        }
    }

    private fun addTrackPlayListCrossRef(playlistId: Long) {
        viewModelScope.launch {
            databaseInteractor.insertPlayListTrackCrossRef(playlistId, trackItem)
        }
    }

    fun addTrackToPlayList(playlist: Playlist) {
        if (clickDebounce()) {
            viewModelScope.launch {
                val isAlreadyInPlayList = async(Dispatchers.IO) {
                    databaseInteractor.checkPlaylistHasTrack(trackItem.trackId, playlist.id)
                }.await()
                if (isAlreadyInPlayList) {
                    renderState(
                        TrackState.TrackInfo(
                            trackItem.trackName, true,
                            playlist.name, null
                        )
                    )
                } else {
                    val id = async(Dispatchers.IO) {
                        databaseInteractor.insertPlayListTrackCrossRef(playlist.id, trackItem)
                    }.await()
                    addTrackPlayListCrossRef(playlist.id)
                    renderState(
                        TrackState.TrackInfo(
                            trackItem.trackName, false,
                            playlist.name, id
                        )
                    )
                }
            }
        }
    }

    private fun processResult(playLists: List<Playlist>) {
        if (playLists.isEmpty()) {
            renderState(
                PlayListsScreenState.Empty("Список плейлистов пуст!")
            )
        } else {
            viewModelScope.launch {
                val updatedPlayList = withContext(Dispatchers.IO) {
                    playLists.map { playlist ->
                        async {
                            playlist.copy(
                                containsCurrentTrack = databaseInteractor.checkPlaylistHasTrack(
                                    trackItem.trackId,
                                    playlist.id
                                )
                            )
                        }
                    }.awaitAll()
                }
                renderState(
                    PlayListsScreenState.Content(updatedPlayList)
                )
            }
        }
    }

    private fun renderState(state: Any) {
        when (state) {
            is PlayListsScreenState -> playlistStateLiveData.postValue(state)
            is TrackState -> checkTrackBelongsToPlaylistLiveData.postValue(state)
        }
    }

    private fun loadTrackScreen(track: String?): Track {
        val trackItem = gson.fromJson<Track>(track, Track::class.java)
        return trackItem
    }

    private fun getCurrentPlayerState(): PlayerState {
        return playerStateLiveData.value ?: PlayerState(
            isLoading = false,
            track = TrackInfoMapper.map(trackItem),
            playStatus = PlayStatus(
                isPrepared = false,
                progress = DateFormatter.timeFormatter.format(0),
                isPlaying = false
            )
        )
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            setIsClickAllowed(true)
        }
        return current
    }

    fun controlFavoriteState() {
        if (trackItem.isFavorite) {
//            removeFromFavorite()
            updateIsFavoriteStatus(false, trackItem)
        } else {
//            saveTrackToFavorites()
            updateIsFavoriteStatus(true, trackItem)
        }
    }

//    private fun saveTrackToFavorites() {
//        if (clickDebounce()) {
//            viewModelScope.launch {
//                trackItem.isFavorite = true
//                databaseInteractor.saveTrackToDatabase(trackItem)
//            }
//        }
//    }

    private fun updateIsFavoriteStatus(isFavorite: Boolean, track: Track){
        if (clickDebounce()){
            viewModelScope.launch {
                databaseInteractor.updateIsFavoriteStatus(isFavorite, track)
            }
        }
    }

//    private fun removeFromFavorite() {
//        if (clickDebounce()) {
//            viewModelScope.launch {
//                trackItem.isFavorite = false
//                databaseInteractor.removeFromFavorite(trackItem)
//            }
//        }
//    }

    fun playerController() {
        if (playerInteractor.isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun loadContent() {
        viewModelScope.launch {
            val track = TrackInfoMapper.map(trackItem)
            databaseInteractor.getFavoriteStatus(track.trackId).collect { isFavorite ->
                playerStateLiveData.value = getCurrentPlayerState().copy(
                    track = track.copy(isInFavorite = isFavorite)
                )
            }
        }
    }

    fun showLoading() {
        playerStateLiveData.value = getCurrentPlayerState().copy(
            isLoading = true,
        )
    }

    fun preparePlayer() {
        playerInteractor.preparePlayer(trackItem.previewUrl)
        playerInteractor.setOnPreparedListener {
            val newState = getCurrentPlayerState()
            playerStateLiveData.value = newState.copy(
                isLoading = false,
                playStatus = newState.playStatus.copy(isPrepared = true)
            )
        }
    }

    fun startPlayer() {
        playerInteractor.playerStart()
        showTimeCountDown()
        val newState = getCurrentPlayerState()
        playerStateLiveData.value = newState.copy(
            playStatus = newState.playStatus.copy(isPlaying = true)
        )
    }

    fun pausePlayer() {
        playerInteractor.playerPause()
        timerJob?.cancel()
        val newState = getCurrentPlayerState()
        playerStateLiveData.value = newState.copy(
            playStatus = newState.playStatus.copy(isPlaying = false)
        )
    }

    private fun showTimeCountDown() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(TIMER_DELAY)
                val newState = getCurrentPlayerState()
                playerStateLiveData.value = newState.copy(
                    playStatus = newState.playStatus.copy(
                        progress = DateFormatter.timeFormatter.format(playerInteractor.getCurrentPosition()),
                        isPlaying = playerInteractor.isPlaying()
                    )
                )

            }
        }

        playerInteractor.setOnCompleteListener {
            timerJob?.cancel()

            playerInteractor.seekTo(0)

            val onTrackComplete = getCurrentPlayerState()
            playerStateLiveData.value = onTrackComplete.copy(
                playStatus = onTrackComplete.playStatus.copy(
                    progress = DateFormatter.timeFormatter.format(0)
                )
            )

            val onPlayerComplete = getCurrentPlayerState()
            playerStateLiveData.value = onPlayerComplete.copy(
                playStatus = onPlayerComplete.playStatus.copy(
                    isPlaying = false
                )
            )
        }
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1_500L
        private const val TIMER_DELAY = 50L
        private val TRACK_TIMER_TOKEN = Any()

    }
}

