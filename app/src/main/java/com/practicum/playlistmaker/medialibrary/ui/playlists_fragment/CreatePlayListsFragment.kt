package com.practicum.playlistmaker.medialibrary.ui.playlists_fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.playlistmaker.databinding.CreatePlaylistFragmentBinding
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.screen_state.create_playlist.CreatePlaylistState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.createplaylists.viewmodel.CreatePlayListsViewModel
import com.practicum.playlistmaker.player.presentation.utils.debounce
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

open class CreatePlayListsFragment : Fragment() {

    var _binding: CreatePlaylistFragmentBinding? = null
    val binding get() = _binding!!

    private val requester = PermissionRequester.instance()
    private lateinit var permissionsNeeded: String

    private var playListName: String = ""
    private var playListDescription: String = ""
    private var coverUri: Uri? = null

    private lateinit var playlist: Playlist
    private var playLists = emptyList<Playlist>()

    private var confirmDialogPlaylistExists: MaterialAlertDialogBuilder? = null

    private lateinit var notificationFadeIn: Animation
    private lateinit var notificationFadeOut: Animation

    private val handler = Handler(Looper.getMainLooper())

    open val viewModel: CreatePlayListsViewModel by viewModel()

    private var isClickAllowed: Boolean = true
    private val setClickTrueDebounce = debounce<Boolean>(
        DEBOUNCE_DELAY_MILLIS,
        lifecycleScope,
        true
    ) { isAllowed ->
        isClickAllowed = isAllowed
    }


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

        notificationFadeIn =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        notificationFadeOut =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri?.let {
                    grantPersistableUriPermission(it)
                    coverUri = uri
                    binding.imagepickArea.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                }
            }

        binding.createPlayListButton.isEnabled = false

        binding.toolbar.setOnClickListener {
            if (clickDebounce()) {
                when {
                    playListName.isNotEmpty() -> notificationFadeIn()
                    coverUri != null -> notificationFadeIn()
                    playListDescription.isNotEmpty() -> notificationFadeIn()
                    else -> {
                        findNavController().navigateUp()
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (clickDebounce()) {
                        when {
                            playListName.isNotEmpty() -> notificationFadeIn()
                            coverUri != null -> notificationFadeIn()
                            playListDescription.isNotEmpty() -> notificationFadeIn()
                            else -> {
                                findNavController().navigateUp()
                            }
                        }
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

        viewModel.checkCurrentPlaylists()

        viewModel.permissionStateLiveData().observe(viewLifecycleOwner) { state ->
            if (state is CreatePlaylistState.Content) {
                this.playLists = state.playLists
            }
            Log.d("PlaylistState", "$state")
            binding.createPlayListButton.setOnClickListener {
                processState(state)

            }
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsNeeded = android.Manifest.permission.READ_MEDIA_IMAGES
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)  {
            permissionsNeeded = android.Manifest.permission.READ_EXTERNAL_STORAGE

        }


        binding.imagepickArea.setOnClickListener {
            lifecycleScope.launch {
                requester.request(permissionsNeeded)
                    .collect { result ->
                        when (result) {
                            is PermissionResult.Granted -> {
                                pickMedia.launch(
                                    arrayOf("image/*")
//                                    PickVisualMediaRequest(
//                                        ActivityResultContracts.PickVisualMedia.ImageOnly
//                                    )
                                )
                            }

                            is PermissionResult.Denied.DeniedPermanently -> {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    data = Uri.fromParts("package", context?.packageName, null)
                                }
                                context?.startActivity(intent)
                            }

                            is PermissionResult.Denied.NeedsRationale -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Разрешение требуется для загрузки обложки плейлистов",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            is PermissionResult.Cancelled -> {
                                return@collect
                            }
                        }
                    }
            }

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

    private fun processState(state: CreatePlaylistState) {
        when (state) {
            is CreatePlaylistState.Content -> {
                playlist = Playlist(0, playListName, playListDescription, coverUri, 0, false)
                val filteredPlaylist = this.playLists.find { it.name == playlist.name }
                Log.d("ViewmodelPlaylist", "$filteredPlaylist")
                if (filteredPlaylist == null) {
                    viewModel.addPlaylistWithReplace(playlist)
                    findNavController().navigateUp()

                } else {
                    confirmDialogPlaylistExists?.show()
                }
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

    open fun endEditing() {
        notificationFadeOut()
        findNavController().navigateUp()
    }

    private fun onCancelButtonPressed() {
        notificationFadeOut()
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "tracklist_covers"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "${playListName.addSuffix("_cover")}")

        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(
                Bitmap.CompressFormat.JPEG,
                30,
                outputStream
            )
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            setClickTrueDebounce(true)
        }
        return current
    }

    private fun String.addSuffix(suffix: String): String {
        return this + suffix
    }

    private fun grantPersistableUriPermission(uri: Uri) {
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

    }
}