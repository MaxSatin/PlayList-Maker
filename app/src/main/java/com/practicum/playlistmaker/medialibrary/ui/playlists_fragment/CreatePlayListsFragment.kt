package com.practicum.playlistmaker.medialibrary.ui.playlists_fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.practicum.playlistmaker.medialibrary.domain.screen_state.CreatePlaylistState
import com.practicum.playlistmaker.medialibrary.presentation.playlists.createplaylists.viewmodel.CreatePlayListsViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class CreatePlayListsFragment : Fragment() {

    private var _binding: CreatePlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private val requester = PermissionRequester.instance()


    private var playListName: String = ""
    private var playListDescription: String = ""
    private var coverUri: Uri? = null

    private lateinit var playlist: Playlist

    private var confirmDialogPlaylistExists: MaterialAlertDialogBuilder? = null
    private var confirmDialogIsPlaylistFinished: MaterialAlertDialogBuilder? = null

    private val viewModel: CreatePlayListsViewModel by viewModel()


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
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    grantPersistableUriPermission(it)
                    coverUri = uri
                    binding.imagepickArea.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                }
            }

        binding.createPlayListButton.isEnabled = false

        binding.toolbar.setOnClickListener{
            when {
                playListName.isNotEmpty() -> confirmDialogIsPlaylistFinished?.show()
                coverUri != null && playListName.isNotEmpty() -> confirmDialogIsPlaylistFinished?.show()
                playListDescription.isNotEmpty() && playListName.isNotEmpty() -> confirmDialogIsPlaylistFinished?.show()
                else -> {}
            }
        }

//        binding.createPlayListButton.setOnClickListener {
////                processState(state)
//            Log.d("Playlistvalue", "$playListName $playListDescription $coverUri")
//            val playlist = Playlist(
//                playListName,
//                playListDescription,
//                coverUri
//            )
//            viewModel.addPlaylistWithReplace(playlist)
//        }


//            val playlist = Playlist(
////                playListName,
////                playListDescription,
////                coverUri
////            )
////            viewModel.checkCurrentPlaylists(playlist)
////            Log.d("PlaylistOnClick", "$playlist")
//
//        val playlist = Playlist(
//            playListName,
//            playListDescription,
//            coverUri
//        )
        viewModel.checkCurrentPlaylists()

        viewModel.permissionStateLiveData().observe(viewLifecycleOwner) { state ->
            Log.d("PlaylistState", "$state")
            binding.createPlayListButton.setOnClickListener {
                processState(state)

            }
//                val playlist = Playlist(
//                    playListName,
//                    playListDescription,
//                    coverUri
//                )
//                viewModel.checkCurrentPlaylists(playlist)
//                Log.d("PlaylistOnClick", "$playlist")

////                val playlist = Playlist(
////                    playListName,
////                    playListDescription,
////                    coverUri
////                )
////                viewModel.checkCurrentPlaylists(playlist)
//            }
//                Log.d("PlaylistOnClick", "$playlist")
//                processState(state)
//                Log.d("Playlistvalue", "$playListName $playListDescription $coverUri")
//                val playlist = Playlist(
//                    playListName,
//                    playListDescription,
//                    coverUri
//                )
//                viewModel.addPlaylistWithReplace(playlist)
        }


        binding.imagepickArea.setOnClickListener {
            lifecycleScope.launch {
                requester.request(android.Manifest.permission.READ_EXTERNAL_STORAGE).collect{ result ->
                    when(result) {
                        is PermissionResult.Granted -> {
                            pickMedia.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                        }
                        is PermissionResult.Denied.DeniedPermanently -> {
                            val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS).apply {
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

        confirmDialogIsPlaylistFinished = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.is_playlistfinished_dialog_hint))
            .setNegativeButton("Завершить") { dialog, which ->
                PlayListsFragment.createArgs(playListName)

                findNavController().navigateUp()
            }
            .setPositiveButton("Отмена") { dialog, which ->
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
                if(!s.isNullOrEmpty()) {
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
        binding.playlistDescriptionInputEditText.addTextChangedListener(playlistDescriptionTextWatcher)
    }

    private fun processState(state: CreatePlaylistState) {
        when (state) {
            is CreatePlaylistState.CopyExists -> confirmDialogPlaylistExists?.show()
            is CreatePlaylistState.NoCopyExists -> {
                playlist = Playlist(
                    playListName,
                    playListDescription,
                    coverUri,
                    0,
                    false
                )
                viewModel.addPlaylistWithReplace(playlist)

            }

            is CreatePlaylistState.Content -> {
                playlist = Playlist(playListName, playListDescription, coverUri,0, false)
                val filteredPlaylist = state.playLists.find { it.name == playlist.name }
                Log.d("ViewmodelPlaylist", "$filteredPlaylist")
                if (filteredPlaylist == null) {
                    viewModel.addPlaylistWithReplace(playlist)
                    findNavController().navigateUp()

                } else {
                    confirmDialogPlaylistExists?.show()
                }
//                confirmDialog?.show()
                Log.d("Playlists", "${state.playLists}")
            }
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
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

    private fun String.addSuffix(suffix: String): String {
        return this + suffix
    }

    private fun grantPersistableUriPermission(uri: Uri) {
        try {
            val contentResolver = requireActivity().contentResolver
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, flags)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        _binding = null
        coverUri = null
        confirmDialogPlaylistExists = null
        confirmDialogIsPlaylistFinished = null
        super.onDestroyView()
    }
}