package com.practicum.playlistmaker.Creator

import android.content.Context
import com.practicum.playlistmaker.data.Constants
import com.practicum.playlistmaker.data.network.TracklistRetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.TracklistRepositoryImpl
import com.practicum.playlistmaker.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.data.storage.impl.ClearStorageImpl
import com.practicum.playlistmaker.data.storage.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.data.storage.impl.SaveTracksHistoryToStorageImpl
import com.practicum.playlistmaker.data.storage.manipulator.ClearLocalStorage
import com.practicum.playlistmaker.data.storage.manipulator.SaveTrackHistoryToStorage
import com.practicum.playlistmaker.domain.interactors.AddTrackToHistoryIntr
import com.practicum.playlistmaker.domain.interactors.GetTrackHistoryIntr
import com.practicum.playlistmaker.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.domain.interactors.SearchTrackListIntr
import com.practicum.playlistmaker.domain.repository.TrackListRepository
import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.domain.use_case.media_player.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.use_case.media_player.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.domain.use_case.tracks_intr.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_intr.GetTrackHistoryFromStorageUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_intr.GetTrackListFromServerUseCase

object Creator {

//    fun provideGson(): Gson {
//        return Gson()
//    }

    fun provideSharedPrefsClient(context: Context, key: String): SharedPrefsClient {
        return SharedPrefsClient(context, key)
    }

    fun provideSaveTrackHistoryToStorage(context: Context): SaveTrackHistoryToStorage{
        return SaveTracksHistoryToStorageImpl(provideSharedPrefsClient(
            context,
            Constants.SHAREDPREFS_TRACKS_HISTORY)
        )
    }

    fun provideTracksHistoryRepository(context: Context): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(provideSharedPrefsClient(
            context,
            Constants.SHAREDPREFS_TRACKS_HISTORY)
        )
    }

    fun provideTrackListRepository(): TrackListRepository{
        return TracklistRepositoryImpl(TracklistRetrofitNetworkClient())
    }

    fun provideSearchTrackListIntr(): SearchTrackListIntr{
        return GetTrackListFromServerUseCase(provideTrackListRepository())

    }

    fun provideAddTrackToHistoryIntr(context: Context): AddTrackToHistoryIntr{
        return AddTrackToHistoryUseCase(
            provideSaveTrackHistoryToStorage(context),
            provideTracksHistoryRepository(context)
        )
    }
    fun provideGetTrackHistoryIntr(context: Context): GetTrackHistoryIntr{
        return GetTrackHistoryFromStorageUseCase(provideTracksHistoryRepository(context))
    }

    fun provideClearLocalStorage(context: Context): ClearLocalStorage{
        return ClearStorageImpl(provideSharedPrefsClient(context,
            Constants.SHAREDPREFS_TRACKS_HISTORY)
        )
    }

    fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository())
    }
}