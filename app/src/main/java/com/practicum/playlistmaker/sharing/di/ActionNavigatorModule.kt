package com.practicum.playlistmaker.sharing.di

import com.practicum.playlistmaker.sharing.domain.interactor.ActionNavigator
import com.practicum.playlistmaker.sharing.domain.interactor.ActionNavigatorImpl
import org.koin.dsl.module

val actionNavigatorModule = module {
    single <ActionNavigator> {
        ActionNavigatorImpl()
    }
}