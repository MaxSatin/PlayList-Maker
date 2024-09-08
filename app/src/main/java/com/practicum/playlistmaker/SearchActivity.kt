package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.data.dto.TrackListResponse
import com.practicum.playlistmaker.data.network.ItunesAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.practicum.playlistmaker.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val KEY = "KEY"
        private const val SEARCH_HISTORY_PREFERENCES = "search_history"
        private const val TRACK_ITEM_KEY = "trackItem"
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

//    private val sharedPrefs by lazy {
//        getSharedPreferences(SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE)
//    }

//    private val iTunesBaseUrl = "https://itunes.apple.com"
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(iTunesBaseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//    private val gson = Gson()
//    private val itunesApiService = retrofit.create(ItunesAPI::class.java)

    val searchTrackList = Creator.provideSearchTrackListIntr()
    val addTrackToHistory = Creator.provideAddTrackToHistoryIntr(this)
    private val gson = Creator.provideGson()

    private val trackList = mutableListOf<CurrentTrack>()


    private val onTrackClickListener = TrackAdapter.OnTrackClickListener { item ->
        if(clickDebounce()) {
            searchHistory.addTracksToHistory(item)
            val track = gson.toJson(item)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_ITEM_KEY, track)
            startActivity(intent)
        }

    }
    private val adapter = TrackAdapter(onTrackClickListener)


    private val onTrackClickListenerHistory = HistoryRVAdapter.OnTrackClickListenerHistory { item ->
        if (clickDebounce()) {
            val track = gson.toJson(item)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_ITEM_KEY, track)
            startActivity(intent)
        }
    }
    private val trackHistoryAdapter = HistoryRVAdapter(onTrackClickListenerHistory)

    private val searchHistory by lazy { SearchHistory(sharedPrefs, trackHistoryAdapter) }

    val searchRunnable = Runnable { searchSongs() }
    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    lateinit var binding: ActivitySearchBinding
    private var textInput = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        enableEdgeToEdge()

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
        trackHistoryAdapter.updateItems(searchHistory.getTracks())

        binding.clearHistorySearchButton?.setOnClickListener {
            searchHistory.updateTracks(emptyList())
            binding.trackHistory.visibility = View.GONE
        }

        binding.buttonSearchBack.setOnClickListener {
            finish()
        }
        binding.clearIcon.setOnClickListener {
            binding.editTextwather.setText("")
            binding.searchResults.visibility = View.GONE

            if (searchHistory.isTrackHistoryEmpty()) {
                binding.trackHistory?.visibility = View.GONE
            }
            trackList.clear()
            adapter.updateItems(trackList)
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        binding.refreshSearchButton.setOnClickListener {
            searchSongs()
        }

        binding.editTextwather.setOnFocusChangeListener { view, hasFocus ->

            binding.trackHistory?.visibility =
                if (hasFocus && binding.editTextwather.text.isEmpty() && !searchHistory.isTrackHistoryEmpty()) View.VISIBLE else View.GONE
        }

        binding.editTextwather.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongs()
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
                    searchDebounce()

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

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showErrorNothingFound(text: String) {
        if (text.isNotEmpty()) {
            trackList.clear()
            adapter.updateItems(trackList)
            binding.searchResults.visibility = View.VISIBLE
            binding.trackHistory?.visibility = View.GONE
            binding.badConnectionPlaceHolder.visibility = View.GONE
            binding.nothingFoundPlaceHolder.visibility = View.VISIBLE
        }
    }

    private fun searchSongs() {
        binding.searchResults.visibility = View.VISIBLE
        binding.progressBar?.visibility = View.VISIBLE
        binding.recyclerSearch.visibility = View.GONE
        binding.nothingFoundPlaceHolder.visibility = View.GONE
        binding.badConnectionPlaceHolder.visibility = View.GONE

        itunesApiService.getSongsList(binding.editTextwather.text.toString()).enqueue(object :
            Callback<TrackListResponse> {
            override fun onResponse(
                call: Call<TrackListResponse>,
                response: Response<TrackListResponse>
            ) {
                binding.progressBar?.visibility = View.GONE
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            binding.recyclerSearch?.visibility = View.VISIBLE
                            binding.trackHistory?.visibility = View.GONE
                            binding.nothingFoundPlaceHolder.visibility = View.GONE
                            binding.badConnectionPlaceHolder.visibility = View.GONE
                            trackList.clear()
                            trackList.addAll(response.body()?.results!!)
                            adapter.updateItems(trackList)
                        } else {
                            showErrorNothingFound(binding.editTextwather.text.toString())
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TrackListResponse>, t: Throwable) {
                showErrorBadConnection()
            }
        })
    }

    private fun showErrorBadConnection() {
        trackList.clear()
        adapter.updateItems(trackList)
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
