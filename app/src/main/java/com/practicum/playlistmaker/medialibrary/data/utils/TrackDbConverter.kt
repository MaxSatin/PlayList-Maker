package com.practicum.playlistmaker.medialibrary.data.utils

import android.net.Uri
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.model.track_model.Track
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.domain.model.playlist_model.Playlist

class TrackDbConverter {

    fun map(trackEntity: TrackEntity): Track {
        return with(trackEntity){
            Track(
                trackId,
                artistName,
                trackName,
                trackTimeMillis,
                previewUrl,
                artworkUrl60,
                artworkUrl100,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                isFavorite,
                null
            )
        }
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return with(playlistEntity) {
            Playlist(
                name,
                description,
                toUriConverter(coverUri),
                trackCount,
                containsCurrentTrack
            )
        }
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return with(playlist){
            PlaylistEntity(
                name,
                description,
                fromUriConverter(coverUri),
                trackCount,
                containsCurrentTrack
            )
        }
    }

    private fun fromUriConverter(uri: Uri?): String {
        return uri.toString()
    }

    private fun toUriConverter(uriString: String?): Uri {
        return Uri.parse(uriString)
    }
}