package com.practicum.playlistmaker.medialibrary.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.medialibrary.domain.track_model.Track
import com.practicum.playlistmaker.medialibrary.ui.fragment.FavoriteTracksFragment
import com.practicum.playlistmaker.medialibrary.ui.fragment.PlayListsFragment

class MediaLibraryViewPagerAdapter(
    private val favoriteTrackList: List<Track>,
    private val playLists: List<List<Track>>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,

): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FavoriteTracksFragment.newInstance(favoriteTrackList)
            1 -> PlayListsFragment.newInstance(playLists)
            else -> FavoriteTracksFragment.newInstance(favoriteTrackList)
        }
    }
}