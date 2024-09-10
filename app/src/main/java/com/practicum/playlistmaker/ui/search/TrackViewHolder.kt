package com.practicum.playlistmaker.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import java.text.SimpleDateFormat
import java.util.Locale
import com.practicum.playlistmaker.domain.model.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = TrackItemBinding.bind(itemView)

    fun bind(item: Track) {
        binding.trackName.text = item.trackName
        binding.trackArtistName.text = item.artistName
        binding.trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        Glide.with(binding.root.context)
            .load(item.artworkUrl60)
            .placeholder(R.drawable.vector_empty_album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.small_corner_radius)))
            .into(binding.trackCoverImage)
    }
}