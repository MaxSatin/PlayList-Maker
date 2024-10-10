package com.practicum.playlistmaker.settings.domain.interactor

interface AppThemeInteractor {
    fun checkIsDarkThemeOn(): Boolean
    fun setAppTheme()
    fun switchAppTheme(isDarkModeEnabled: Boolean)
}