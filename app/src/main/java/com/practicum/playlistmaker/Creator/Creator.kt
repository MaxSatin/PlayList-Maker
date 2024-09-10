package com.practicum.playlistmaker.Creator

import android.content.Context
import com.practicum.playlistmaker.data.network.TracklistRetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.AppThemeRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracklistRepositoryImpl
import com.practicum.playlistmaker.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.data.storage.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.domain.repository.TrackListRepository
import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.domain.repository.AppThemeRepository
import com.practicum.playlistmaker.domain.use_case.app_theme.AppThemeInteractorImpl
import com.practicum.playlistmaker.domain.use_case.interactor.AppThemeInteractor
import com.practicum.playlistmaker.domain.use_case.media_player.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.use_case.media_player.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.domain.use_case.tracks_intr.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_intr.CheckIsHistoryEmptyUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_intr.ClearHistoryUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_intr.GetTrackHistoryFromStorageUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_intr.GetTrackListFromServerUseCase

object Creator {

//    fun provideGson(): Gson {
//        return Gson()
//    }

    private const val APP_THEME = "app_theme"
    private const val SHAREDPREFS_TRACKS_HISTORY = "history_track_list_shared_prefs"


    fun provideSharedPrefsClient(context: Context, key: String): SharedPrefsClient {
        return SharedPrefsClient(context, key)
    }

    fun provideAddTrackToHistoryUseCase(context: Context): AddTrackToHistoryUseCase{
        return AddTrackToHistoryUseCase(
            provideTracksHistoryRepository(
            context)
        )
    }
    fun provideCheckIsHistoryEmptyUseCase(context: Context): CheckIsHistoryEmptyUseCase{
        return CheckIsHistoryEmptyUseCase(
            provideTracksHistoryRepository(
                context)
        )
    }
    fun provideClearHistoryUseCase(context: Context): ClearHistoryUseCase{
        return ClearHistoryUseCase(
            provideTracksHistoryRepository(
                context)
        )
    }
    fun provideGetTrackHistoryFromStorageUseCase(context: Context): GetTrackHistoryFromStorageUseCase{
        return GetTrackHistoryFromStorageUseCase(
            provideTracksHistoryRepository(
                context)
        )
    }
    fun provideGetTrackListFromServerUseCase(): GetTrackListFromServerUseCase{
        return GetTrackListFromServerUseCase(
            provideTrackListRepository()
        )
    }

    fun provideAppThemeInteractor(context: Context): AppThemeInteractor{
        return AppThemeInteractorImpl(provideAppThemeRepository(context))
    }

    private fun provideAppThemeRepository(context: Context) : AppThemeRepository{
        return AppThemeRepositoryImpl(provideSharedPrefsClient(context, APP_THEME))
    }

    private fun provideTracksHistoryRepository(context: Context): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(provideSharedPrefsClient(
            context,
            SHAREDPREFS_TRACKS_HISTORY)
        )
    }

    private fun provideTrackListRepository(): TrackListRepository{
        return TracklistRepositoryImpl(TracklistRetrofitNetworkClient())
    }

//    fun provideSearchTrackListIntr(): SearchTrackListIntr{
//        return GetTrackListFromServerUseCase(provideTrackListRepository())
//
//    }

//    fun provideAddTrackToHistoryIntr(context: Context): AddTrackToHistoryIntr{
//        return AddTrackToHistoryUseCase(
//            provideSaveTrackHistoryToStorage(context),
//            provideTracksHistoryRepository(context)
//        )
//    }
//    fun provideGetTrackHistoryIntr(context: Context): GetTrackHistoryIntr{
//        return GetTrackHistoryFromStorageUseCase(provideTracksHistoryRepository(context))
//    }

//    fun provideClearLocalStorage(context: Context): ClearLocalStorage{
//        return ClearStorageImpl(provideSharedPrefsClient(context,
//            Constants.SHAREDPREFS_TRACKS_HISTORY)
//        )
//    }

    private fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository())
    }

}