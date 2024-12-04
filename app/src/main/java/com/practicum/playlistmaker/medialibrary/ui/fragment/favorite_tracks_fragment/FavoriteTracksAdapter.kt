package com.practicum.playlistmaker.medialibrary.ui.fragment.favorite_tracks_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import com.practicum.playlistmaker.databinding.TrackItemBinding

class FavoriteTracksAdapter(
    private val onTrackClicked: (track: Track) -> Unit,
) : RecyclerView.Adapter<FavoriteTrackViewHolder>() {

    private var favoriteTrackList = emptyList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTrackViewHolder {
        val binding = TrackItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteTrackViewHolder(binding) { position: Int ->
            if (position != RecyclerView.NO_POSITION)
                favoriteTrackList.getOrNull(position)?.let { track ->
                    onTrackClicked(track)
                }

        }
    }

    override fun getItemCount(): Int = favoriteTrackList.size

    override fun onBindViewHolder(holder: FavoriteTrackViewHolder, position: Int) {
        favoriteTrackList.getOrNull(position)?.let { track ->
            holder.bind(track)
        }
    }

    fun updateItems(items: List<Track>) {

        val oldItems = this.favoriteTrackList
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
        this.favoriteTrackList = newItems
        diffResult.dispatchUpdatesTo(this)
    }

}