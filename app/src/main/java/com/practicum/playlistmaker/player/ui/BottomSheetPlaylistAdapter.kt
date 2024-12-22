package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.practicum.playlistmaker.databinding.PlaylistPlayerFragmentItemBinding
import com.practicum.playlistmaker.medialibrary.ui.playlists_fragment.PlaylistViewHolder
import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist

class BottomSheetPlaylistAdapter(
    private val onPlaylistClick: (playlist: Playlist) -> Unit,
) : RecyclerView.Adapter<BottomSheetPlaylistVH>() {

    private var playlists = emptyList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetPlaylistVH {
        val binding = PlaylistPlayerFragmentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BottomSheetPlaylistVH(binding) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                playlists.getOrNull(position)?.let { playlist: Playlist ->
                    onPlaylistClick(playlist)
                }
            }
        }
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: BottomSheetPlaylistVH, position: Int) {
        playlists.getOrNull(position)?.let { playlist ->
            holder.bind(playlist)
        }
    }

    fun updateItems(playlists: List<Playlist>) {
        val oldItems = this.playlists
        val newItems = playlists
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldItems.size

            override fun getNewListSize(): Int = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].name == newItems[newItemPosition].name
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].containsCurrentTrack == newItems[newItemPosition].containsCurrentTrack
            }

        })

        this.playlists = playlists
        diffResult.dispatchUpdatesTo(this)
    }
}
