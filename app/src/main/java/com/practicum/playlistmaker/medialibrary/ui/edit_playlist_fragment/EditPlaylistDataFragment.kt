package com.practicum.playlistmaker.medialibrary.ui.edit_playlist_fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import com.practicum.playlistmaker.databinding.CreatePlaylistFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.edit_playlist_data_state.EditPlaylistDataState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.edit_playlist.EditPlaylistDataViewModel
import com.practicum.playlistmaker.medialibrary.ui.playlists_fragment.CreatePlayListsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistDataFragment: CreatePlayListsFragment() {

    override val viewModel: EditPlaylistDataViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = CreatePlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistGson = arguments?.getString(PLAY_LIST_KEY)
        if(playlistGson != null)
        viewModel.initializePlaylistData(playlistGson)

        viewModel.getPlayListInfoLiveData().observe(viewLifecycleOwner){ state ->
            processState(state)
        }

    }

    private fun processState(state: EditPlaylistDataState){
        when(state){
            is EditPlaylistDataState.Content -> loadContent(state)
        }
    }

    private fun loadContent(state: EditPlaylistDataState.Content){
        binding.toolbar.title = state.fragmentTitle
        binding.createPlayListButton.text = state.buttonTitle
        uploadImage(state.playlist.coverUri, binding.imagepickArea)
    }

    private fun uploadImage(uri: Uri?, imageView: ImageView){
        val inputStream = requireContext().contentResolver.openInputStream(uri!!)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        imageView.setImageBitmap(bitmap)
        inputStream?.close()
    }

    companion object {
        private val keyObject: Any = Unit
        private const val ANIMATION_DELAY_MILLIS = 100L
        private const val DEBOUNCE_DELAY_MILLIS = 2_000L
        private const val PLAY_LIST_KEY = "playlist_key"

        fun createArgs(playlist: String?) = bundleOf(
            PLAY_LIST_KEY to playlist
        )
    }
}