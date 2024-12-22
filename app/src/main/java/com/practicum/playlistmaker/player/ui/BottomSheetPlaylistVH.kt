package com.practicum.playlistmaker.player.ui

import android.net.Uri
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistPlayerFragmentItemBinding
import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist


class BottomSheetPlaylistVH(
    private val binding: PlaylistPlayerFragmentItemBinding,
    private val onPlaylistClicked: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onPlaylistClicked(bindingAdapterPosition)
        }
    }

    fun bind(playlist: Playlist) {
        Log.d("UriVH", "${playlist.coverUri}")
        with(binding) {
            playlistName.text = playlist.name
            trackNumber.text = playlist.trackCount.toString()
            Glide.with(binding.root.context)
                .load(playlist.coverUri)
                .placeholder(R.drawable.vector_empty_album_placeholder)
                .fitCenter()
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.radius_8)))
                .into(playlistCover)

            if(playlist.containsCurrentTrack){
                binding.checkIndicator.isVisible = true
            }

        }

    }

    private fun toUriConverter(uriString: String?): Uri {
        return Uri.parse(uriString)
    }

    private fun fromUriConverter(uri: Uri?): String {
        return uri.toString()
    }

}
