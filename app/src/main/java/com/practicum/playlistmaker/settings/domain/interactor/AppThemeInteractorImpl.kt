package com.practicum.playlistmaker.settings.domain.interactor

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.repository.AppThemeRepository

class AppThemeInteractorImpl(
    private val appThemeRepository: AppThemeRepository
) : AppThemeInteractor {

    private var isDarkTheme = appThemeRepository.checkIsDarkThemeOn()

    override fun checkIsDarkThemeOn(): Boolean {
        return appThemeRepository.checkIsDarkThemeOn()
    }

    override fun setAppTheme() {
        appThemeRepository.setAppTheme()
//        AppCompatDelegate.setDefaultNightMode(
//            if (isDarkTheme) {
//                AppCompatDelegate.MODE_NIGHT_YES
//            } else {
//                AppCompatDelegate.MODE_NIGHT_NO
//            }
//        )
    }

    override fun switchAppTheme(isDarkModeEnabled: Boolean) {
        appThemeRepository.switchAppTheme(isDarkModeEnabled)
//        isDarkTheme = isDarkModeEnabled
//        appThemeRepository.setDarkTheme(isDarkTheme)
//        AppCompatDelegate.setDefaultNightMode(
//            if (isDarkModeEnabled) {
//                AppCompatDelegate.MODE_NIGHT_YES
//            } else {
//                AppCompatDelegate.MODE_NIGHT_NO
//            }
//        )
    }
}

