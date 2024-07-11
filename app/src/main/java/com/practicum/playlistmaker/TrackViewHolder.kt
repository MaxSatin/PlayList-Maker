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
    var onTrackClickListener: OnTrackClickListener? = null

    fun bind(item: CurrentTrack) {
        binding.trackName.text = item.trackName
        binding.trackArtistName.text = item.artistName
        binding.trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        Glide.with(binding.root.context)
            .load(item.artworkUrl60)
            .placeholder(R.drawable.vector_empty_album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.small_corner_radius)))
            .into(binding.trackCoverImage)

        binding.root.setOnClickListener{
            onTrackClickListener?.onTrackClick(item)
        }
    }

    fun interface OnTrackClickListener {
        fun onTrackClick(item: CurrentTrack)
    }
}