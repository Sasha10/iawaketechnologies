package com.notyteam.iawaketechnologies.utils

import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

inline fun <reified T : ViewModel> FragmentActivity.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory)[T::class.java]
}

inline fun <reified T : ViewModel> androidx.fragment.app.Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory)[T::class.java]
}

fun <T> LiveData<T>.observe(@NonNull owner: LifecycleOwner, onUpdate: (t: T) -> Unit) {
    this.observe(owner, Observer { data ->
        try {
            data?.let { onUpdate(data) }
        } catch (e: Exception) {

        }
    })
}