package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.player.presentation.model.Track
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import org.koin.dsl.module

//val consumerDataModule = module {
//    single <ConsumerData<List<Track>>> { (trackList: List<Track>) ->
//        ConsumerData.Data(value = trackList)
//    }
//
//    single <ConsumerData<String>> { (error: String) ->
//        ConsumerData.Error(message = error)
//    }
//
//    single <ConsumerData<String>> { (message: String) ->
//        ConsumerData.NoConnection(message = message)
//    }
//}