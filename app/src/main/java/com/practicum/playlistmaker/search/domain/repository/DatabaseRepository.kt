package com.practicum.playlistmaker.search.domain.repository

import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    fun getFavoriteTracksId(): Flow<List<String>>
}