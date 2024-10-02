package com.practicum.playlistmaker.search.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.track_model.Track
import com.practicum.playlistmaker.search.presentation.state.State
import com.practicum.playlistmaker.search.presentation.view_model.SearchViewModel


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val KEY = "KEY"
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }


//    private val getTrackList = Creator.provideGetTrackListFromServerUseCase()
//    private val addTrackToHistory by lazy { Creator.provideAddTrackToHistoryUseCase(this) }
//    private val clearHistory by lazy { Creator.provideClearHistoryUseCase(this) }
//    private val getTracksHistory by lazy { Creator.provideGetTrackHistoryFromStorageUseCase(this) }
//    private val checkIsHistoryEmpty by lazy { Creator.provideCheckIsHistoryEmptyUseCase(this)}
//    private val gson = GsonProvider.gson


//    private val trackList = mutableListOf<Track>()
    private val viewModel by viewModels<SearchViewModel> { SearchViewModel.getSearchViewModelFactory() }
//    private lateinit var viewModel : SearchViewModel
    private lateinit var adapter: TrackAdapter
    private lateinit var trackHistoryAdapter: HistoryRVAdapter
//    private val adapter = TrackAdapter(viewModel::showTrackPlayer)

    //    { item ->
////        if (clickDebounce()) {
////            addTrackToHistory(item)
////            trackHistoryAdapter.updateItems(getTracksHistory())
////            showPlayer(item)
////        }
////    }
//    private val trackHistoryAdapter = HistoryRVAdapter(viewModel::showTrackPlayer)
//    { item ->
//        showPlayer(item)
//    }


//    val searchRunnable = Runnable { searchSongs() }
//    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true
    private var isHistoryEmpty: Boolean = false

    lateinit var binding: ActivitySearchBinding
    private var textInput = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        enableEdgeToEdge()

//        viewModel by viewModels<SearchViewModel> { SearchViewModel.getSearchViewModelFactory() }
//        viewModel = ViewModelProvider(this, SearchViewModel.getSearchViewModelFactory())[SearchViewModel::class.java]
        adapter = TrackAdapter(viewModel::showTrackPlayer)
        trackHistoryAdapter = HistoryRVAdapter(viewModel::showTrackPlayer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottom = if (ime.bottom > 0) {
                ime.bottom
            } else {
                systemBars.bottom
            }
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottom)
            insets
        }

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        binding.recyclerSearch.adapter = adapter
        binding.trackHistoryRV?.adapter = trackHistoryAdapter


//        trackHistoryAdapter.updateItems(getTracksHistory())

        viewModel.observeTrackSearchState().observe(this) { trackListState ->
            render(trackListState)
        }

        viewModel.observeHistoryState().observe(this) { historyListState ->
            if(historyListState is State.HistoryListState.Empty) {
                isHistoryEmpty = true
            } else {
                isHistoryEmpty = false
                render(historyListState)
            }
        }

        viewModel.getShowTrackPlayerTrigger().observe(this) { track ->
            showPlayer(track)
        }


        binding.clearHistorySearchButton?.setOnClickListener {
            viewModel.clearHistoryList()
//            clearHistory()
//            trackHistoryAdapter.updateItems(getTracksHistory())
            binding.trackHistory.visibility = View.GONE
        }

        binding.buttonSearchBack.setOnClickListener {
            finish()
        }
        binding.clearIcon.setOnClickListener {
            binding.editTextwather.setText("")
            binding.searchResults.visibility = View.GONE

            if (isHistoryEmpty) {
                binding.trackHistory?.visibility = View.GONE
            }
//            trackList.clear()
//            adapter.updateItems(trackList)
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        binding.refreshSearchButton.setOnClickListener {
            viewModel.searchTracks(textInput)
        }

        binding.editTextwather.setOnFocusChangeListener { view, hasFocus ->

            binding.trackHistory?.visibility =
                if (hasFocus && binding.editTextwather.text.isEmpty() && !isHistoryEmpty) View.VISIBLE else View.GONE
        }

        binding.editTextwather.setOnEditorActionListener { v, actionId, event ->
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
                if (s.isNullOrEmpty()) {
                    binding.editTextwather.setBackgroundColor(getColor(R.color.grey_pale))
                } else {
                    textInput = s.toString()
                    viewModel.searchDebounce(textInput)

                }
                val isFocusedAndEmpty =
                    if (binding.editTextwather.hasFocus() && s?.isEmpty() == true) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                binding.trackHistory?.visibility = isFocusedAndEmpty

                binding.clearIcon.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.editTextwather.addTextChangedListener(editTextWatcher)

    }

    private fun render(state: State) {
        when (state) {
            is State.SearchListState.Loading -> {
                showLoading()
//                    binding.searchResults.visibility = View.VISIBLE
//                    binding.progressBar?.visibility = View.VISIBLE
//                    binding.recyclerSearch.visibility = View.GONE
//                    binding.nothingFoundPlaceHolder.visibility = View.GONE
//                    binding.badConnectionPlaceHolder.visibility = View.GONE
            }

            is State.SearchListState.Content -> {
                showTracks(state.tracks)
            }

            is State.SearchListState.NoConnection -> {
                showErrorBadConnection()
//                    binding.badConnectionPlaceHolder.visibility = View.VISIBLE
//                    binding.searchResults.visibility = View.VISIBLE
//                    binding.trackHistory?.visibility = View.GONE
//                    binding.nothingFoundPlaceHolder.visibility = View.GONE
//                    binding.progressBar?.visibility = View.GONE
            }

            is State.SearchListState.Empty -> {
                showErrorNothingFound(textInput)
//                    binding.searchResults.visibility = View.VISIBLE
//                    binding.nothingFoundPlaceHolder.visibility = View.VISIBLE
//                    binding.trackHistory?.visibility = View.GONE
//                    binding.badConnectionPlaceHolder.visibility = View.GONE
            }

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
        PlayerActivity.show(this, trackGson)
//        val track = gson.toJson(item)
//        val intent = Intent(this, PlayerActivity::class.java)
//        intent.putExtra(TRACK_ITEM_KEY, track)
//        startActivity(intent)
    }

//    private fun clickDebounce(): Boolean {
//        val current = isClickAllowed
//        if (isClickAllowed) {
//            isClickAllowed = false
//            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
//        }
//        return current
//    }

//    private fun searchDebounce() {
//        handler.removeCallbacks(searchRunnable)
//        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
//    }

    private fun showErrorNothingFound(text: String) {
        if (text.isNotEmpty()) {
//            viewModel.clearSearchList()
////            trackList.clear()
//            adapter.updateItems(trackList)
            binding.searchResults.visibility = View.VISIBLE
            binding.progressBar?.visibility = View.GONE
            binding.trackHistory?.visibility = View.GONE
            binding.badConnectionPlaceHolder.visibility = View.GONE
            binding.nothingFoundPlaceHolder.visibility = View.VISIBLE
        }
    }

//    private fun searchSongs() {
//        binding.searchResults.visibility = View.VISIBLE
//        binding.progressBar?.visibility = View.VISIBLE
//        binding.recyclerSearch.visibility = View.GONE
//        binding.nothingFoundPlaceHolder.visibility = View.GONE
//        binding.badConnectionPlaceHolder.visibility = View.GONE

//        getTrackList(
//            expression = binding.editTextwather.text.toString(),
//            consumer = object : Consumer<List<Track>> {
//                override fun consume(data: ConsumerData<List<Track>>) {
//                    val currentRunnable = searchRunnable
//                    if (currentRunnable != null) {
//                        handler.removeCallbacks(currentRunnable)
//                    }
//                    val newSearchRunnable = Runnable {
//                        binding.progressBar?.visibility = View.GONE
//                        when (data) {
//                            is ConsumerData.Error -> showError(data.message)
//                            is ConsumerData.NoConnection -> showErrorBadConnection()
//                            is ConsumerData.Data -> {
//                                if (data.value.isNullOrEmpty()) {
//                                    showErrorNothingFound(binding.editTextwather.text.toString())
//                                } else {
//                                    binding.recyclerSearch?.visibility = View.VISIBLE
//                                    binding.trackHistory?.visibility = View.GONE
//                                    binding.nothingFoundPlaceHolder.visibility = View.GONE
//                                    binding.badConnectionPlaceHolder.visibility = View.GONE
//                                    trackList.clear()
//                                    trackList.addAll(data.value!!)
//                                    adapter.updateItems(trackList)
//                                }
//                            }
//
//
//                        }
//                    }
//                    handler.post(newSearchRunnable)
//                }
//            }
//        )
//
//    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        onBackPressedDispatcher.onBackPressed()
    }

    private fun showErrorBadConnection() {
//        viewModel.clearSearchList()
//        trackList.clear()
//        adapter.updateItems(trackList)
        binding.searchResults.visibility = View.VISIBLE
        binding.trackHistory?.visibility = View.GONE
        binding.nothingFoundPlaceHolder.visibility = View.GONE
        binding.badConnectionPlaceHolder.visibility = View.VISIBLE
        binding.progressBar?.visibility = View.GONE
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textInput = savedInstanceState.getString(KEY, textInput)
    }

}
