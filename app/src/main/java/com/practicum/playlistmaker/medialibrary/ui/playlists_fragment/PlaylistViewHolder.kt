package com.practicum.playlistmaker.medialibrary.ui.playlists_fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistItemBinding
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val trackNumber: Int,
    private val onPlaylistClicked: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onPlaylistClicked(bindingAdapterPosition)
        }
    }

    fun bind(playlist: Playlist) {

        with(binding) {
            playlistName.text = playlist.name
            tracksNumbers.text = trackNumber.toString()

            Glide.with(binding.root.context)
                .load(playlist.coverUri)
                .placeholder(R.drawable.vector_empty_album_placeholder)
                .fitCenter()
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.radius_8)))
                .into(playlistCover)
        }

    }

}