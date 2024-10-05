package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.search.domain.track_model.Track

class HistoryRVAdapter(
    private val onTrackClicked: (track: Track) -> Unit,
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var historyTrackList: List<Track> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val binding = TrackItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TrackViewHolder(binding) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                historyTrackList.getOrNull(position)?.let { track: Track ->
                    onTrackClicked(track)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return historyTrackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        historyTrackList.getOrNull(position)?.let { track ->
            holder.bind(track)
        }
    }

    fun updateItems(items: List<Track>) {
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
    fun interface OnTrackClickListenerHistory {
        fun onTrackClick(trackItem: Track)
    }

}