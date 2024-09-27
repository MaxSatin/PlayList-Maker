package com.practicum.playlistmaker.player.domain.mapper

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
    val timeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
}