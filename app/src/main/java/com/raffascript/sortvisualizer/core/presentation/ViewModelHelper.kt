package com.raffascript.sortvisualizer.core.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras

fun <VM : ViewModel> viewModelFactory(initializer: (SavedStateHandle) -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return initializer(extras.createSavedStateHandle()) as T
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun <VM : LifecycleObserver> VM.observeLifecycleEvents(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@observeLifecycleEvents)
        onDispose {
            lifecycle.removeObserver(this@observeLifecycleEvents)
        }
    }
}