package com.practicum.playlistmaker.player.data.utils

import android.net.Uri
import com.practicum.playlistmaker.player.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.player.data.db.entity.TrackEntity
import com.practicum.playlistmaker.player.domain.model.playlist_model.Playlist
import com.practicum.playlistmaker.player.domain.model.track_model.Track


class TrackDbConverter {

    fun map(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
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
                playListName
            )
        }
    }

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
                playListName
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