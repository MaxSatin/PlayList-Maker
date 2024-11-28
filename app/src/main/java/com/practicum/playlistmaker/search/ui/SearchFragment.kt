package com.practicum.playlistmaker.search.ui
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.databinding.SearchFragmentBinding
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.presentation.state.State
import com.practicum.playlistmaker.search.presentation.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val KEY = "KEY"
    }

    private val viewModel: SearchViewModel by viewModel()


    private lateinit var adapter: TrackAdapter
    private lateinit var trackHistoryAdapter: HistoryRVAdapter

    private var isHistoryEmpty: Boolean = false

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private var editTextWatcher: TextWatcher? = null

    private var textInput = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TrackAdapter(viewModel::showTrackPlayer)
        trackHistoryAdapter = HistoryRVAdapter(viewModel::showTrackPlayer)
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        binding.recyclerSearch.adapter = adapter
        binding.trackHistoryRV?.adapter = trackHistoryAdapter

        viewModel.observeTrackSearchState().observe(viewLifecycleOwner) { trackListState ->
            render(trackListState)
            Log.d("trackListState", "$trackListState")
        }

        viewModel.observeHistoryState().observe(viewLifecycleOwner) { historyListState ->

            when (historyListState) {
                is State.HistoryListState.Empty -> {
                    isHistoryEmpty = true
                    hideHistory()
                }
                is State.HistoryListState.Content -> {
                    isHistoryEmpty = false
                    render(historyListState)
                }
            }
        }

        viewModel.getShowTrackPlayerTrigger().observe(viewLifecycleOwner) { track ->
            showPlayer(track)
        }

        binding.clearHistorySearchButton?.setOnClickListener {
            viewModel.clearHistoryList()
            isHistoryEmpty = true
            binding.trackHistory.visibility = View.GONE
        }


        binding.clearIcon.setOnClickListener {
            binding.editTextwatcher.setText("")
            binding.searchResults.visibility = View.GONE
            if (isHistoryEmpty == true) {
                binding.trackHistory?.visibility = View.GONE
            } else {
                binding.trackHistory?.visibility = View.VISIBLE
            }

            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        binding.refreshSearchButton.setOnClickListener {
            viewModel.searchTracks(textInput)
        }

        binding.editTextwatcher.setOnFocusChangeListener { view, hasFocus ->

            binding.trackHistory?.visibility =
                if (hasFocus && binding.editTextwatcher.text.isEmpty() && !isHistoryEmpty) View.VISIBLE else View.GONE

        }

        binding.editTextwatcher.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTracks(textInput)
                true
            }
            false
        }

        val editTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, ncout: Int) {

                if (binding.editTextwatcher.hasFocus() && (s?.isEmpty() == true) && !isHistoryEmpty) {
                    binding.searchResults.visibility = View.GONE
                    binding.nothingFoundPlaceHolder.visibility = View.GONE
                    binding.badConnectionPlaceHolder.visibility = View.GONE
                    binding.trackHistory.visibility = View.VISIBLE
                } else {
                    binding.searchResults.visibility = View.GONE
                }
                textInput = s.toString()
                viewModel.searchDebounce(textInput)
                binding.clearIcon.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.editTextwatcher.addTextChangedListener(editTextWatcher)

    }

    private fun render(state: State) {
        when (state) {
            is State.SearchListState.Loading -> showLoading()
            is State.SearchListState.Content -> showTracks(state.tracks)
            is State.SearchListState.NoConnection -> showErrorBadConnection()
            is State.SearchListState.Empty -> showErrorNothingFound(textInput)
            is State.SearchListState.Error -> showError(state.errorMessage)
            is State.HistoryListState.Content -> showHistory(state.tracks)
            is State.HistoryListState.Empty -> hideHistory()
        }
    }

    private fun showHistory(tracks: List<Track>) {
        trackHistoryAdapter.updateItems(tracks)

    }

    private fun hideHistory() {
        binding.trackHistory.visibility = View.GONE
    }

    private fun showLoading() {
        binding.searchResults.visibility = View.VISIBLE
        binding.progressBar?.visibility = View.VISIBLE
        binding.recyclerSearch.visibility = View.GONE
        binding.nothingFoundPlaceHolder.visibility = View.GONE
        binding.badConnectionPlaceHolder.visibility = View.GONE
    }

    private fun showTracks(trackList: List<Track>) {
        adapter.updateItems(trackList)
        binding.recyclerSearch?.visibility = View.VISIBLE
        binding.progressBar?.visibility = View.GONE
        binding.trackHistory?.visibility = View.GONE
        binding.nothingFoundPlaceHolder.visibility = View.GONE
        binding.badConnectionPlaceHolder.visibility = View.GONE
    }

    private fun showPlayer(trackGson: String) {
        PlayerActivity.show(requireContext(), trackGson)
    }

    private fun showErrorNothingFound(text: String) {
        if (text.isNotEmpty()) {
            binding.searchResults.visibility = View.VISIBLE
            binding.progressBar?.visibility = View.GONE
            binding.trackHistory?.visibility = View.GONE
            binding.badConnectionPlaceHolder.visibility = View.GONE
            binding.nothingFoundPlaceHolder.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorBadConnection() {
        if (textInput.isNotEmpty()) {
            binding.searchResults.visibility = View.VISIBLE
            binding.trackHistory?.visibility = View.GONE
            binding.nothingFoundPlaceHolder.visibility = View.GONE
            binding.badConnectionPlaceHolder.visibility = View.VISIBLE
            binding.progressBar?.visibility = View.GONE
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY, textInput)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editTextWatcher?.let { binding.editTextwatcher.removeTextChangedListener(it) }
        _binding = null
    }

}
