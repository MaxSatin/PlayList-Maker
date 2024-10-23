package com.practicum.playlistmaker.medialibrary.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMedialibraryBinding
import com.practicum.playlistmaker.medialibrary.presentation.viewmodel.MediaLibraryViewModel
import com.practicum.playlistmaker.medialibrary.ui.viewpager.MediaLibraryViewPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedialibraryBinding
    private val viewModel: MediaLibraryViewModel by viewModel()
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMedialibraryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolBar.setNavigationOnClickListener{
            finish()
        }

        viewModel.getMediaLibraryStateLiveData().observe(this) { state ->
            binding.viewPager.adapter = MediaLibraryViewPagerAdapter(
                state.favoriteTracks,
                state.playList,
                supportFragmentManager,
                lifecycle = lifecycle
            )
            tabLayoutMediator =
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.text = "Избранные треки"
                        1 -> tab.text = "Плейлисты"
                    }
                }
            tabLayoutMediator.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}