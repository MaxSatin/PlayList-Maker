package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.search.di.dataModule
import com.practicum.playlistmaker.search.di.interactorModule
import com.practicum.playlistmaker.search.di.repositoryModule
import com.practicum.playlistmaker.search.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private val appThemeInteractor by lazy { Creator.provideAppThemeInteractor(this) }

    override fun onCreate() {
        super.onCreate()
        appThemeInteractor.setAppTheme()


        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}