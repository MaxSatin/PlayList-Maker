package com.practicum.playlistmaker.medialibrary.ui.edit_playlist_fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.create_playlist.CreatePlaylistState
import com.practicum.playlistmaker.medialibrary.domain.screen_state.edit_playlist_data_state.EditPlaylistDataState
import com.practicum.playlistmaker.medialibrary.domain.screen_state.playlist_details.PlaylistState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.edit_playlist.EditPlaylistDataViewModel
import com.practicum.playlistmaker.medialibrary.ui.playlist_details_fragment.PlaylistDetailsFragment
import com.practicum.playlistmaker.medialibrary.ui.playlists_fragment.CreatePlayListsFragment
import com.practicum.playlistmaker.player.presentation.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class EditPlaylistDataFragment : CreatePlayListsFragment() {

    override val viewModel: EditPlaylistDataViewModel by viewModel()

    private val requester = PermissionRequester.instance()

    private var playlistId: Long = 0L
    private var initPlayListName: String = ""
    private var initPlayListDescription: String = ""
    private var initCoverUri: Uri? = null

    private var playListName: String = ""
    private var playListDescription: String = ""
    private var coverUri: Uri? = null

    private lateinit var playlist: Playlist
    private var playLists = emptyList<Playlist>()

    private var confirmDialogPlaylistExists: MaterialAlertDialogBuilder? = null

    private lateinit var notificationFadeIn: Animation
    private lateinit var notificationFadeOut: Animation

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed: Boolean = true
    private val setClickTrueDebounce = debounce<Boolean>(
        DEBOUNCE_DELAY_MILLIS,
        lifecycleScope,
        true
    ) { isAllowed ->
        isClickAllowed = isAllowed
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationFadeIn =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        notificationFadeOut =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri?.let {
                    takePersistableUriPermission(it)
                    coverUri = uri
                    upLoadImage(uri, binding.imagepickArea)
//                    binding.imagepickArea.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                    Log.d("ViewmodelCoverUri", "$coverUri")
                }
            }

        binding.createPlayListButton.isEnabled = false

        playlistId = arguments?.getLong(PLAY_LIST_KEY) ?: 0L

        viewModel.loadPlaylistDetailsState(playlistId)

        viewModel.getPlaylistDetailsLiveData().observe(viewLifecycleOwner) { playlistState ->
            processPlayListData(playlistState)
        }
        binding.toolbar.setOnClickListener {
            if (clickDebounce()) {
                endEditing()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (clickDebounce()) {
                       endEditing()
                    }
                }
            })


        binding.endCreatingButton.setOnClickListener {
            endEditing()
        }
        binding.cancel.setOnClickListener {
            onCancelButtonPressed()
            isClickAllowed = true
        }

//        viewModel.checkCurrentPlaylists()

        viewModel.permissionStateLiveData().observe(viewLifecycleOwner) { state ->
            if (state is CreatePlaylistState.Content) {
                this.playLists = state.playLists
            }
            Log.d("PlaylistState", "$state")
            binding.createPlayListButton.setOnClickListener {
                processState(state)

            }
        }


        binding.imagepickArea.setOnClickListener {
            pickMedia.launch(arrayOf("image/*"))
//            lifecycleScope.launch {
//                requester.request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                    .collect { result ->
//                        when (result) {
//                            is PermissionResult.Granted -> {
//                                pickMedia.launch(
//                                    arrayOf("image/*")
//                                )
//                            }
//
//                            is PermissionResult.Denied.DeniedPermanently -> {
//                                val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS).apply {
//                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                    data = Uri.fromParts("package", context?.packageName, null)
//                                }
//                                context?.startActivity(intent)
//                            }
//
//                            is PermissionResult.Denied.NeedsRationale -> {
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Разрешение требуется для загрузки обложки плейлистов",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//
//                            is PermissionResult.Cancelled -> {
//                                return@collect
//                            }
//                        }
//                    }
//            }

        }

        confirmDialogPlaylistExists = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.create_playlist_dialog_hint))
            .setNegativeButton("Нет") { dialog, which ->
            }
            .setPositiveButton("Да") { dialog, which ->
                val playlistCopy = playlist.copy(name = playListName.addSuffix("_copy"))
                viewModel.addPlaylist(playlistCopy)
                findNavController().navigateUp()
            }

        val playlistNameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.createPlayListButton.isEnabled = true
                    playListName = s.toString().trim()
                } else {
                    binding.createPlayListButton.isEnabled = false
                }
                Log.d("Playlistname", "$playListName")
                Log.d("PlaylistnameInit", "$playlistId")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        val playlistDescriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                playListDescription = s.toString().trim()
                Log.d("Playlistdescription", "$playListDescription")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.playlistNameInputEditText.addTextChangedListener(playlistNameTextWatcher)
        binding.playlistDescriptionInputEditText.addTextChangedListener(
            playlistDescriptionTextWatcher
        )
    }

    private fun processPlayListData(state: EditPlaylistDataState) {
        when (state) {
            is EditPlaylistDataState.Content -> loadPlayListData(state)
            is EditPlaylistDataState.Empty -> Toast.makeText(
                requireContext(),
                "Информация отсутсвует",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun loadPlayListData(state: EditPlaylistDataState.Content) {
        with(state.playlist) {
            playlistId = id
            initPlayListName = name
            binding.playlistNameInputEditText.setText(initPlayListName)
            initPlayListDescription = description
            binding.playlistDescriptionInputEditText.setText(initPlayListDescription)
            initCoverUri = coverUri
//            takePersistableUriPermission(coverUri!!)
            upLoadImage(initCoverUri, binding.imagepickArea)
        }
        binding.toolbar.title = state.fragmentTitle
        binding.createPlayListButton.text = state.buttonTitle
    }

    private fun upLoadImage(uri: Uri?, imageView: ImageView) {
        Glide.with(binding.root.context)
            .load(uri)
            .placeholder(R.drawable.vector_empty_album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(8))
            .into(imageView)
    }

    private fun processState(state: CreatePlaylistState) {
        when (state) {
            is CreatePlaylistState.Content -> {
//                playlist = Playlist(
//                    playlistId, this.playListName, playListDescription, coverUri, 0, false)
//                val filteredPlaylist = this.playLists.find { it.name == this.playListName }

                Log.d("ViewmodelInitName", "$initPlayListName")
                Log.d("Viewmodelname", "$playListName")
                Log.d("ViewmodelCoverUri", "$coverUri")

//                if (filteredPlaylist == null) {
//                    if (playListName != initPlayListName || playListDescription != initPlayListDescription || coverUri != initCoverUri) {
//                        viewModel.addPlaylistWithReplace(playlist)
                val playlistNameUpdated =
                    if (!initPlayListName.equals(playListName)) playListName else initPlayListName
                val playlistDescriptionUpdated =
                    if (!initPlayListDescription.equals(playListDescription)) playListDescription else initPlayListDescription
                val posterUriUpdated =
                    if (initCoverUri != coverUri && coverUri != null) coverUri else initCoverUri
                viewModel.updatePlaylist(
                    playlistId,
                    playlistNameUpdated,
                    playlistDescriptionUpdated,
                    posterUriUpdated.toString()
                )

                findNavController().navigate(
                    R.id.action_editPlaylistDataFragment_to_playlistDetailsFragment,
                    PlaylistDetailsFragment.createArgs(playlistId)
                )

//                    }
//                } else if(playListName == initPlayListName && playListDescription == initPlayListDescription && coverUri == initCoverUri) {
//                    confirmDialogPlaylistExists?.show()
//                }
                Log.d("Playlists", "${state.playLists}")
            }
        }
    }

    private fun checkExistsAndSet() {
//        playlist = Playlist(playListName, playListDescription, coverUri, 0, false)
        val filteredPlaylist = playLists.find { it.name == playlist.name }
        Log.d("ViewmodelPlaylist", "$filteredPlaylist")
        if (filteredPlaylist == null) {
            viewModel.addPlaylistWithReplace(playlist)
            findNavController().navigateUp()

        } else {
            confirmDialogPlaylistExists?.show()
        }
    }

    private fun notificationFadeIn() {
        handler.removeCallbacksAndMessages(keyObject)
        handler.postDelayed(
            {
                binding.notificationDialog.startAnimation(notificationFadeIn)
                binding.notificationDialog.isVisible = true
            },
            keyObject,
            ANIMATION_DELAY_MILLIS
        )
    }

    private fun notificationFadeOut() {
        handler.postDelayed(
            {
                binding.notificationDialog.startAnimation(notificationFadeOut)
                binding.notificationDialog.isVisible = false
            },
            keyObject,
            ANIMATION_DELAY_MILLIS
        )
    }

    override fun endEditing() {
        findNavController().navigate(
            R.id.action_editPlaylistDataFragment_to_playlistDetailsFragment,
            PlaylistDetailsFragment.createArgs(playlistId)
        )
    }

    private fun onCancelButtonPressed() {
        notificationFadeOut()
    }


    private fun String.addSuffix(suffix: String): String {
        return this + suffix
    }

    private fun takePersistableUriPermission(uri: Uri) {
        try {
            val contentResolver = requireActivity().contentResolver
            val flags =
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, flags)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        _binding = null
        coverUri = null
        confirmDialogPlaylistExists = null
        handler.removeCallbacksAndMessages(keyObject)
        super.onDestroyView()
    }

    companion object {
        private val keyObject: Any = Unit
        private const val ANIMATION_DELAY_MILLIS = 100L
        private const val DEBOUNCE_DELAY_MILLIS = 2_000L
        private const val PLAY_LIST_KEY = "playlist_key"

        fun createArgs(playlistId: Long) = bundleOf(
            PLAY_LIST_KEY to playlistId
        )
    }
}