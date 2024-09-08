package com.practicum.playlistmaker.domain.use_case.tracks_intr

import com.practicum.playlistmaker.data.storage.manipulator.ClearLocalStorage
import com.practicum.playlistmaker.domain.interactors.ClearHistoryIntr

class ClearHistoryUseCase(
    private val clearLocalStorage: ClearLocalStorage
): ClearHistoryIntr {
    override fun clearHistory() {
        clearLocalStorage.clearStorage()
    }
}