package com.practicum.playlistmaker

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.TrackItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = TrackItemBinding.bind(itemView)

    fun bind(item: CurrentTrack) {
        binding.trackName.text = item.trackName
        binding.trackName.isSelected = true
        binding.trackArtistName.text = item.artistName
        binding.trackArtistName.isSelected = true
        binding.trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        Glide.with(binding.root.context)
            .load(
                if(item.artworkUrl60.isNullOrEmpty()){
                    R.drawable.vector_empty_album_placeholder
                } else {
                    item.artworkUrl60
                }
            )
            .placeholder(R.drawable.placeholder_loading_icon)
            .fitCenter()
            .transform(RoundedCorners(binding.root.resources.getDimensionPixelSize(R.dimen.small_corner_radius)))
            .into(binding.trackCoverImage)
    }
}