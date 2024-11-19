package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.practicum.playlistmaker.search.data.dto.NetworkResponse
import com.practicum.playlistmaker.search.data.dto.TrackListRequest
import com.practicum.playlistmaker.search.data.repository.TracklistNetworkClient
import com.practicum.playlistmaker.search.domain.track_model.Resourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response

class TracklistRetrofitNetworkClient(
    private val context: Context,
    private val iTunesApi: ItunesAPI,
) : TracklistNetworkClient {
    override suspend fun doTrackRequest(dto: Any?): NetworkResponse {
        if (!isConnected()) {
            return NetworkResponse().apply { resultCode = -1 }
        }
        return when (dto) {
            is TrackListRequest ->  withContext(Dispatchers.IO) {
                    try {
                        val response = iTunesApi.getSongsList(dto.expression)
//                val body = response.body() ?: NetworkResponse()
                        response.apply { resultCode = 200 }
                    } catch (ex: Exception) {
                        NetworkResponse().apply { resultCode = 503 }
                    }
                }
            else -> NetworkResponse().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}





