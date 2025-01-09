package com.practicum.playlistmaker.medialibrary.ui.playlist_details_fragment

import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import com.practicum.playlistmaker.medialibrary.presentation.utils.CustomGestureListener
import com.practicum.playlistmaker.medialibrary.ui.favorite_tracks_fragment.FavoriteTrackViewHolder

class TrackListAdapter(
//    private val onTrackClicked: (track: Track) -> Unit,
    private val onSingleTap: (track: Track) -> Unit,
    private val onLongPress: (track: Track) -> Unit,
) : RecyclerView.Adapter<TrackItemViewHolder>() {

    private var gestureDetector: GestureDetector? = null
    private var favoriteTrackList = emptyList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackItemViewHolder {
        val binding = TrackItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackItemViewHolder(
            binding,
            onSingleTap = { position: Int ->
                if (position != RecyclerView.NO_POSITION) {
                    favoriteTrackList.getOrNull(position)?.let { track ->
                        onSingleTap(track)
                    }
                }
            },
            onLongPress = { position: Int ->
                if (position != RecyclerView.NO_POSITION) {
                    favoriteTrackList.getOrNull(position)?.let { track ->
                        onLongPress(track)
                    }
                }
            }
        )
    }

    override fun getItemCount(): Int = favoriteTrackList.size

    override fun onBindViewHolder(holder: TrackItemViewHolder, position: Int) {

        if (position != RecyclerView.NO_POSITION) {
            favoriteTrackList.getOrNull(position)?.let { track ->
                holder.bind(track)
            }

//            gestureDetector = GestureDetector(
//                holder.itemView.context,
//                object : CustomGestureListener(
//                    onSingleTap = { onSingleTap(favoriteTrackList[holder.bindingAdapterPosition]) },
//                    onLongPress = { onLongPress(favoriteTrackList[holder.bindingAdapterPosition]) }
//                ) {
//                    override fun onLongPress(e: MotionEvent) {
//                        onLongPress(favoriteTrackList[holder.bindingAdapterPosition])
//                        super.onLongPress(e)
//                    }
//
//                    override fun onSingleTapUp(e: MotionEvent): Boolean {
//                        onSingleTap(favoriteTrackList[holder.bindingAdapterPosition])
//                        return super.onSingleTapUp(e)
//                    }
//                }
//            )



//            holder.itemView.setOnTouchListener { _, event ->
//                gestureDetector?.onTouchEvent(event)
//                true
//            }
        }


//            val gestureDetector = GestureDetector(
//                holder.itemView.context,
//                CustomGestureListener(
//                    onSingleTap = { onSingleTap(position) },
//                    onLongPress = { onLongPress(position) }
//                )
//            )


    }

    fun updateItems(items: List<Track>) {

        val oldItems = this.favoriteTrackList
        val newItems = items.toMutableList()
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].trackName == newItems[newItemPosition].trackName
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }
        })
        this.favoriteTrackList = newItems
        diffResult.dispatchUpdatesTo(this)
    }

}