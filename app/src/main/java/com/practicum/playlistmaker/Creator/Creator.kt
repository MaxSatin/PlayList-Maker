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
import com.practicum.playlistmaker.domain.repository.MediaPlayerRepository
import com.practicum.playlistmaker.domain.repository.TrackListRepository
import com.practicum.playlistmaker.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.data.repository.MediaPlayerRepositoryImpl
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

    private fun provideTracksHistoryRepository(context: Context): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(provideSharedPrefsClient(
            context,
            Constants.SHAREDPREFS_TRACKS_HISTORY)
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