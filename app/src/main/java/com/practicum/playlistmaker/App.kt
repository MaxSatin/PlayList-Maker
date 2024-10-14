package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.player.di.playerRepositoryModule
import com.practicum.playlistmaker.player.di.playerInteractorModule
import com.practicum.playlistmaker.player.di.playerViewModelModule
import com.practicum.playlistmaker.search.di.dataModule
import com.practicum.playlistmaker.search.di.interactorModule
import com.practicum.playlistmaker.search.di.repositoryModule
import com.practicum.playlistmaker.search.di.viewModelModule
import com.practicum.playlistmaker.settings.di.settingsViewModelModule
import com.practicum.playlistmaker.settings.di.themeDataModule
import com.practicum.playlistmaker.settings.di.themeInteractorModule
import com.practicum.playlistmaker.settings.di.themeRepositoryModule
import com.practicum.playlistmaker.settings.domain.interactor.AppThemeInteractor
import com.practicum.playlistmaker.sharing.di.actionInteractorModule
import com.practicum.playlistmaker.sharing.di.actionRepositoryModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private val appThemeInteractor: AppThemeInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule,
                playerViewModelModule, playerRepositoryModule, playerInteractorModule,
                themeDataModule, themeRepositoryModule, themeInteractorModule, settingsViewModelModule,
                actionRepositoryModule, actionInteractorModule)
        }
        appThemeInteractor.setAppTheme()
    }

}