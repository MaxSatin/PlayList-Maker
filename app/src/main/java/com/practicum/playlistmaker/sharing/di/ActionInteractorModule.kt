package com.practicum.playlistmaker.sharing.di

import com.practicum.playlistmaker.sharing.domain.interactor.ActionInteractor
import com.practicum.playlistmaker.sharing.domain.interactor.ActionInteractorImpl
import org.koin.dsl.module

val actionInteractorModule = module {
    single <ActionInteractor> {
        ActionInteractorImpl(get())
    }
}