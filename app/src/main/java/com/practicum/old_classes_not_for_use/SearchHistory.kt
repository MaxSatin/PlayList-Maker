package com.practicum.old_classes_not_for_use//package com.practicum.playlistmaker
//
//import android.content.SharedPreferences
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//class SearchHistory(
//    private val sharedPrefs: SharedPreferences,
//    private val historyTracksAdapter: HistoryRVAdapter
//) {
//
//    companion object {
//        const val KEY_HISTORY_TRACK_LIST = "history_track_list"
//    }
//
//    private val gson: Gson = Gson()
//    private var historyTracks: List<Track> = getTracks()
//
//
//    fun addTracksToHistory(track: Track) {
//        val index = historyTracks.indexOfFirst { it.trackId == track.trackId }
//        if (index >= 0) {
//            val newHistoryTrackList = historyTracks.toMutableList()
//            newHistoryTrackList.removeAt(index)
//            newHistoryTrackList.add(0, track)
//            updateTracks(newHistoryTrackList)
//        } else if (historyTracks.size > 9) {
//            val newHistoryTrackList = historyTracks.toMutableList()
//            newHistoryTrackList.removeAt(9)
//            newHistoryTrackList.add(0, track)
//            updateTracks(newHistoryTrackList)
//        } else {
//            val newHistoryTrackList = historyTracks.toMutableList()
//            newHistoryTrackList.add(0, track)
//            updateTracks(newHistoryTrackList)
//        }
//
//        saveTrackToLocalStorage(historyTracks)
//    }
//
//    fun updateTracks(newTracks: List<Track>) {
//        historyTracks = newTracks
//        historyTracksAdapter.updateItems(historyTracks)
//        saveTrackToLocalStorage(historyTracks)
//    }
//
//    fun saveTrackToLocalStorage(tracks: List<Track>) {
//        val trackToGson: String = gson.toJson(tracks)
//        sharedPrefs.edit()
//            .putString(KEY_HISTORY_TRACK_LIST, trackToGson)
//            .apply()
//    }
//
//    fun getTrackFromLocalStorage(): List<Track>? {
//        val tracksFromGson: String? = sharedPrefs.getString(KEY_HISTORY_TRACK_LIST, null)
//        return tracksFromGson?.let {
//            val itemType = object : TypeToken<List<Track>>() {}.type
//            gson.fromJson(tracksFromGson, itemType)
//        }
//    }
//
//    fun getTracks(): List<Track> {
//        val tracksFromStorage = getTrackFromLocalStorage()
//        return if (!tracksFromStorage.isNullOrEmpty()) {
//            tracksFromStorage
//        } else {
//            val emptyTrackList = emptyList<Track>()
//            emptyTrackList
//        }
//    }
//
//    fun isTrackHistoryEmpty(): Boolean {
//        return historyTracks.isNullOrEmpty()
//    }
//}
//
//
