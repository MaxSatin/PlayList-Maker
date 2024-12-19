package com.practicum.playlistmaker.medialibrary.ui.playlists_fragment

import com.practicum.playlistmaker.databinding.PlaylistItemBinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist

class PlaylistAdapter(
    private val onPlaylistClick: (playlist: Playlist) -> Unit,
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    private var playlists = emptyList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding, playlists.size) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                playlists.getOrNull(position)?.let { playlist: Playlist ->
                    onPlaylistClick(playlist)
                }
            }
        }
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        playlists.getOrNull(position)?.let { playlist ->
            holder.bind(playlist)
        }
    }
}