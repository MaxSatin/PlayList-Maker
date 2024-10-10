package com.practicum.playlistmaker.search.domain.track_model

sealed interface Resourse<T> {
    data class Success <T>(val data: T): Resourse<T>
    data class Error<T>(val message: String): Resourse<T>
    data class NoConnection<T>(val message: String): Resourse<T>
}