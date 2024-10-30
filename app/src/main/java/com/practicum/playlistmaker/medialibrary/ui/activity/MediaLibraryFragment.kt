package com.practicum.playlistmaker.medialibrary.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.MedialibraryFragmentBinding
import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.MediaLibraryViewModel
import com.practicum.playlistmaker.medialibrary.ui.viewpager.MediaLibraryViewPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment : Fragment() {

    private var _binding: MedialibraryFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MediaLibraryViewModel by viewModel()
    private lateinit var tabLayoutMediator: TabLayoutMediator

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

        viewModel.getMediaLibraryStateLiveData().observe(viewLifecycleOwner) { state ->
            binding.viewPager.adapter = MediaLibraryViewPagerAdapter(
                state.favoriteTracks,
                state.playList,
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
    }
}



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityMedialibraryBinding.inflate(LayoutInflater.from(this))
//        setContentView(binding.root)
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        binding.toolBar.setNavigationOnClickListener {
//            finish()
//        }
//
//        viewModel.getMediaLibraryStateLiveData().observe(this) { state ->
//            binding.viewPager.adapter = MediaLibraryViewPagerAdapter(
//                state.favoriteTracks,
//                state.playList,
//                supportFragmentManager,
//                lifecycle = lifecycle
//            )
//            tabLayoutMediator =
//                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//                    when (position) {
//                        0 -> tab.text = getString(R.string.favorite_tracks)
//                        1 -> tab.text = getString(R.string.playlists)
//                    }
//                }
//            tabLayoutMediator.attach()
//        }
//    }


