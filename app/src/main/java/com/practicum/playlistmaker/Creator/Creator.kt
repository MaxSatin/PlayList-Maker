package com.practicum.playlistmaker.Creator

import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmaker.search.data.network.TracklistRetrofitNetworkClient
import com.practicum.playlistmaker.settings.data.repository.AppThemeRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracklistRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.search.data.storage.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.search.domain.repository.TrackListRepository
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.player.data.repository.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.settings.domain.repository.AppThemeRepository
import com.practicum.playlistmaker.settings.domain.interactor.AppThemeInteractorImpl
import com.practicum.playlistmaker.settings.domain.interactor.AppThemeInteractor
import com.practicum.playlistmaker.player.domain.player_interactor.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.player_interactor.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.tracks_intr.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.CheckIsHistoryEmptyUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.ClearHistoryUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackHistoryFromStorageUseCase
import com.practicum.playlistmaker.search.domain.tracks_intr.GetTrackListFromServerUseCase
import com.practicum.playlistmaker.sharing.domain.interactor.ActionNavigator
import com.practicum.playlistmaker.sharing.domain.interactor.ActionNavigatorImpl

object Creator {

    private const val APP_THEME = "app_theme"
    private const val SHAREDPREFS_TRACKS_HISTORY = "history_track_list_shared_prefs"

    fun provideSharedPrefsClient(context: Context, key: String): SharedPrefsClient {
        return SharedPrefsClient(context, key)
    }

    fun provideAddTrackToHistoryUseCase(context: Context): AddTrackToHistoryUseCase {
        return AddTrackToHistoryUseCase(
            provideTracksHistoryRepository(
                context
            )
        )
    }

    fun provideCheckIsHistoryEmptyUseCase(context: Context): CheckIsHistoryEmptyUseCase {
        return CheckIsHistoryEmptyUseCase(
            provideTracksHistoryRepository(
                context
            )
        )
    }

    fun provideClearHistoryUseCase(context: Context): ClearHistoryUseCase {
        return ClearHistoryUseCase(
            provideTracksHistoryRepository(
                context
            )
        )
    }

    fun provideGetTrackHistoryFromStorageUseCase(context: Context): GetTrackHistoryFromStorageUseCase {
        return GetTrackHistoryFromStorageUseCase(
            provideTracksHistoryRepository(
                context
            )
        )
    }

    fun provideGetTrackListFromServerUseCase(): GetTrackListFromServerUseCase {
        return GetTrackListFromServerUseCase(
            provideTrackListRepository()
        )
    }

    fun provideAppThemeInteractor(context: Context): AppThemeInteractor {
        return AppThemeInteractorImpl(provideAppThemeRepository(context))
    }

    private fun provideAppThemeRepository(context: Context): AppThemeRepository {
        return AppThemeRepositoryImpl(provideSharedPrefsClient(context, APP_THEME))
    }

    private fun provideTracksHistoryRepository(context: Context): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(
            provideSharedPrefsClient(
                context,
                SHAREDPREFS_TRACKS_HISTORY
            )
        )
    }

    private fun provideTrackListRepository(): TrackListRepository {
        return TracklistRepositoryImpl(TracklistRetrofitNetworkClient())
    }

    private fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository())
    }

    fun provideActionNavigator(): ActionNavigator {
        return ActionNavigatorImpl()
    }

}