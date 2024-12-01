package com.practicum.playlistmaker.search.domain.repository

import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    fun checkIsFavorite(trackId: String): Flow<Boolean>
}