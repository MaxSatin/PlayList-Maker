package com.practicum.playlistmaker.search.domain.repository

interface DatabaseRepository {

    suspend fun checkIsInFavorites(trackId: String)
}