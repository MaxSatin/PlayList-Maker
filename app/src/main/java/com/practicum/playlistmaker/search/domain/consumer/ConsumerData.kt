package com.practicum.playlistmaker.search.domain.consumer

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T?) : ConsumerData<T>
    data class Error<T>(val message: String) : ConsumerData<T>
    data class NoConnection<T>(val message: String) : ConsumerData<T>
}