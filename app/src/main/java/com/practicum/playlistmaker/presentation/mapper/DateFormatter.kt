package com.practicum.playlistmaker.presentation.mapper

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
    val timeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
}