package com.practicum.playlistmaker.medialibrary.ui.playlists_fragment

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistItemBinding
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import java.util.Locale

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val onPlaylistClicked: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onPlaylistClicked(bindingAdapterPosition)
        }
    }

    fun bind(playlist: Playlist) {
    Log.d("PlaylistVH","${playlist.coverUri}")
        with(binding) {
            playlistName.text = playlist.name
            tracksNumbers.text =
                String.format(
                    Locale.getDefault(),
                    "%d %s", playlist.trackCount,
                    attachWordEnding(playlist.trackCount)
                )
            Glide.with(binding.root.context)
                .load(playlist.coverUri)
                .placeholder(R.drawable.vector_empty_album_placeholder)
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.padding_8)))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(playlistCover)
        }

    }

    private fun attachWordEnding(trackNumber: Int): String {
        return when {
            trackNumber % 10 == 0 -> "треков"
            trackNumber % 10 == 1 -> "трек"
            trackNumber % 10 in 2..4 -> "трека"
            trackNumber % 10 in 5..9 -> "треков"
            trackNumber % 100 in 11..19 -> "треков"
            else -> ""
        }
    }

}