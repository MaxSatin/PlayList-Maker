package com.practicum.playlistmaker.search.domain.database_interactor

import kotlinx.coroutines.flow.Flow

interface DatabaseInteractor {

    fun checkIsFavorite(trackId: String): Flow<Boolean>
}