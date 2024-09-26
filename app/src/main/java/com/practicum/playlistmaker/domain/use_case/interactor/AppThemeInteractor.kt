package com.practicum.playlistmaker.domain.use_case.interactor

interface AppThemeInteractor {
    fun setAppTheme()
    fun isDarkThemeOn(): Boolean
    fun switchAppTheme(isDarkMode: Boolean)
}