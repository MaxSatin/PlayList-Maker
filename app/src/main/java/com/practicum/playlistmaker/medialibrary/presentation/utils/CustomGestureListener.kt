package com.practicum.playlistmaker.medialibrary.presentation.utils

import android.view.GestureDetector
import android.view.MotionEvent
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track

//class CustomGestureListener(
//    private val onSingleTap: (position: Int) -> Unit,
//    private val onLongPress: (position: Int) -> Unit,
//) : GestureDetector.SimpleOnGestureListener() {
//
//    override fun onSingleTapUp(e: MotionEvent): Boolean {
//        onSingleTap(-1)
//        return false
//    }
//
//    override fun onLongPress(e: MotionEvent) {
//        onLongPress(-1)
//        super.onLongPress(e)
//    }
//}

open class CustomGestureListener(
    private val onSingleTap: (track: Track) -> Unit,
    private val onLongPress: (track: Track) -> Unit,
) : GestureDetector.SimpleOnGestureListener()
