package com.practicum.playlistmaker.medialibrary.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryViewPagerAdapter(
    private val fragmentList: MutableList<Fragment>,
//    private val favoriteListScreenState: FavoriteListScreenState,
//    private val playLists: List<List<Track>>?,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,

): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
//        return when(position){
//            0 ->  FavoriteTracksFragment.newInstance(favoriteListScreenState)
//           else -> PlayListsFragment.newInstance(playLists)
//        }
    }
}