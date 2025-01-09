package com.practicum.playlistmaker.medialibrary.ui.playlist_details_fragment

import android.view.GestureDetector
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackItemViewHolder(
    private val binding: TrackItemBinding,
//    private val gestureDetector: GestureDetector
    onSingleTap: (position: Int) -> Unit,
    onLongPress: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

//    init{
//        itemView.setOnTouchListener { _, e ->
////            onTrackClicked(bindingAdapterPosition)
//            gestureDetector.onTouchEvent(e)
//            true
//        }
//    }

    init {
        itemView.apply {
            setOnClickListener {
                onSingleTap(bindingAdapterPosition)
            }
            setOnLongClickListener {
                onLongPress(bindingAdapterPosition)
                true
            }
        }
    }

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