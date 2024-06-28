package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val KEY = "KEY"
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesApiService = retrofit.create(ItunesAPI::class.java)
    private val trackList = mutableListOf<CurrentTrack>()
    private val adapter = TrackAdapter()


    private lateinit var refreshSearchButton: Button
    private lateinit var recyclerSearch: RecyclerView
    private lateinit var buttonBack: Button
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var nothingFoundPH: LinearLayout
    private lateinit var badConnectionPH: LinearLayout
    private var textInput = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

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

        buttonBack = findViewById(R.id.buttonSearchBack)
        queryInput = findViewById(R.id.editTextwather)
        clearButton = findViewById(R.id.clearIcon)
        recyclerSearch = findViewById(R.id.recyclerSearch)
        refreshSearchButton = findViewById(R.id.refreshSearchButton)
        nothingFoundPH = findViewById(R.id.nothingFoundPlaceHolder)
        badConnectionPH = findViewById(R.id.badConnectionPlaceHolder)


        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        recyclerSearch.adapter = adapter


        buttonBack.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            queryInput.setText("")
            trackList.clear()
            adapter.updateItems(trackList)
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        refreshSearchButton.setOnClickListener {
            searchSongs()
        }

        queryInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongs()
                true
            }
            false
        }

        val editTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    queryInput.setBackgroundColor(getColor(R.color.grey_pale))
                } else {
                    textInput = s.toString()

                }
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        queryInput.addTextChangedListener(editTextWatcher)
    }

    private fun showErrorNothingFound(text: String){
        if(text.isNotEmpty()){
            trackList.clear()
            adapter.updateItems(trackList)
            badConnectionPH.visibility = View.GONE
            nothingFoundPH.visibility = View.VISIBLE
        }
    }

    private fun searchSongs() {
        itunesApiService.getSongsList(queryInput.text.toString()).enqueue(object :
            Callback<TrackListResponse> {
            override fun onResponse(
                call: Call<TrackListResponse>,
                response: Response<TrackListResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            nothingFoundPH.visibility = View.GONE
                            badConnectionPH.visibility = View.GONE
                            trackList.clear()
                            trackList.addAll(response.body()?.results!!)
                            adapter.updateItems(trackList)
                        } else {
                            showErrorNothingFound(queryInput.text.toString())
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
            nothingFoundPH.visibility = View.GONE
            badConnectionPH.visibility = View.VISIBLE
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