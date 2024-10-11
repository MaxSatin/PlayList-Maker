package com.practicum.playlistmaker.sharing.di

import com.practicum.playlistmaker.sharing.domain.repository.ActionRepository
import com.practicum.playlistmaker.sharing.data.repository.ActionRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val actionRepositoryModule = module {
    single <ActionRepository> {
        ActionRepositoryImpl()
    }
}