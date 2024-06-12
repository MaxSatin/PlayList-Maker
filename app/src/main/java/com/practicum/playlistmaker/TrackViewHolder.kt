package com.practicum.playlistmaker

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.TrackItemBinding

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = TrackItemBinding.bind(itemView)

    fun bind(item: TrackItem) {
        binding.trackName.text = item.trackName
        binding.trackArtistName.text = item.artistName
        binding.trackLength.text = item.trackLength
        Glide.with(binding.root.context)
            .load(item.imageCoverUrl)
            .placeholder(R.drawable.placeholder_loading_icon)
            .into(binding.trackCoverImage)
    }
}