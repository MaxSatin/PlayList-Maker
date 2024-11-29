package com.practicum.playlistmaker.medialibrary.ui.root_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.MedialibraryFragmentBinding
import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.MediaLibraryViewModel
import com.practicum.playlistmaker.medialibrary.ui.fragment.favorite_tracks_fragment.FavoriteTracksFragment
import com.practicum.playlistmaker.medialibrary.ui.fragment.PlayListsFragment
import com.practicum.playlistmaker.medialibrary.ui.viewpager.MediaLibraryViewPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment : Fragment() {

    private var _binding: MedialibraryFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MediaLibraryViewModel by viewModel()
    private lateinit var tabLayoutMediator: TabLayoutMediator

    private val fragmentList: MutableList<Fragment> = mutableListOf(
        FavoriteTracksFragment(),
        PlayListsFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MedialibraryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.getMediaLibraryStateLiveData().observe(viewLifecycleOwner) { state ->
            binding.viewPager.adapter = MediaLibraryViewPagerAdapter(
                fragmentList,
//                state.favoriteListScreenState,
//                state.playList,
                parentFragmentManager,
                lifecycle = lifecycle
            )
            tabLayoutMediator =
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.text = getString(R.string.favorite_tracks)
                        1 -> tab.text = getString(R.string.playlists)
                    }
                }
            tabLayoutMediator.attach()
//        }
    }

//    private fun stateProcessor(state: State): Pair<>

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
        _binding = null
    }
}


