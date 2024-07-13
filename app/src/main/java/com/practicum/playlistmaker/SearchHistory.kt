package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.databinding.ActivitySearchBinding

class SearchHistory (
    val sharedPrefs: SharedPreferences,
    val historyTracksAdapter: HistoryRVAdapter
) {

    companion object {
        const val SEARCH_HISTORY = "search_history"
        const val HISTORY_TRACK_LIST = "history_track_list"
    }

    private val gson: Gson = Gson()
    private var historyTracks: List<CurrentTrack> = emptyList<CurrentTrack>()


    fun addTracksToHistory(track: CurrentTrack) {
        val index = historyTracks.indexOfFirst { it.trackId == track.trackId }
        if (index >= 0) {
            val newHistoryTrackList = historyTracks.toMutableList()
            newHistoryTrackList.removeAt(index)
            newHistoryTrackList.add(0,track)
            updateTracks(newHistoryTrackList)
        } else if (index < 0 && historyTracks.size > 9) {
            val newHistoryTrackList = historyTracks.toMutableList()
            newHistoryTrackList.removeAt(9)
            newHistoryTrackList.add(0, track)
            updateTracks(newHistoryTrackList)
        } else {
            val newHistoryTrackList = historyTracks.toMutableList()
            newHistoryTrackList.add(0, track)
            updateTracks(newHistoryTrackList)
        }

        saveTrackToLocalStorage(historyTracks)
    }

    fun updateTracks(newTracks: List<CurrentTrack>) {
        historyTracks = newTracks
        historyTracksAdapter.updateItems(historyTracks)
        saveTrackToLocalStorage(historyTracks)
    }

    fun saveTrackToLocalStorage(tracks: List<CurrentTrack>) {
        val trackToGson: String = gson.toJson(tracks)
            sharedPrefs.edit()
            .putString(HISTORY_TRACK_LIST, trackToGson)
            .apply()
    }

    fun getTrackFromLocalStorage(): List<CurrentTrack>? {
        val tracksFromGson: String? = sharedPrefs.getString(HISTORY_TRACK_LIST, null)
        return tracksFromGson?.let{
            val itemType = object : TypeToken<List<CurrentTrack>>() {}.type
            gson.fromJson(tracksFromGson, itemType)
        }
    }

    fun getTracks(): List<CurrentTrack> {
        val tracksFromStorage = getTrackFromLocalStorage()
        return if (!tracksFromStorage.isNullOrEmpty()) {
            tracksFromStorage
        } else {
            val emptyTrackList = emptyList<CurrentTrack>()
            emptyTrackList
        }
    }
}

class HistoryRVAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    private var historyTrackList: List<CurrentTrack> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val trackView =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(trackView)
    }

    override fun getItemCount(): Int {
        return historyTrackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(historyTrackList[position])

    }

    fun updateItems(items: List<CurrentTrack>) {
        val oldItems = this.historyTrackList
        val newItems = items.toMutableList()
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].trackName == newItems[newItemPosition].trackName
            }

            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }

        })
        this.historyTrackList = newItems
        diffResult.dispatchUpdatesTo(this)
    }

}
