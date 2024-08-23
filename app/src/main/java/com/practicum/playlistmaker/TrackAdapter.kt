package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(private val onTrackClickListener: OnTrackClickListener)
    : RecyclerView.Adapter<TrackViewHolder>() {

    private var trackList: List<CurrentTrack> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {
            onTrackClickListener?.onTrackClick(trackList[holder.adapterPosition])
        }
    }

    fun updateItems(items: List<CurrentTrack>) {

        val oldItems = this.trackList
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

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }
        })
        this.trackList = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface OnTrackClickListener {
        fun onTrackClick(item: CurrentTrack)
    }

}