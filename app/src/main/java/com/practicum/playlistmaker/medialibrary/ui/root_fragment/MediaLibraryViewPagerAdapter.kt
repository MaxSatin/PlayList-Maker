package com.practicum.playlistmaker.medialibrary.ui.root_fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryViewPagerAdapter(
    private val fragmentList: MutableList<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,

): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}