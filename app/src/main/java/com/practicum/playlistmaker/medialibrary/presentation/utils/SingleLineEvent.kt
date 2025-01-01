package com.practicum.playlistmaker.medialibrary.presentation.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLineEvent <T> : MediatorLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    override fun postValue(value: T?) {
        pending.set(true)
        super.postValue(value)
    }

    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }
}
