package org.alba.hortus.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Observes a [Flow] of events and triggers an action for each emitted event.
 *
 * This composable function is designed to simplify the process of observing and reacting to events emitted from a Flow within a Jetpack Compose context.
 * It automatically manages the lifecycle of the coroutine that collects the events and ensures that the `onEvent` action is executed on the main thread.
 *
 * @param events The Flow of events to be observed.
 * @param key1 An optional key to trigger recomposition when the key changes.
 * @param key2 Another optional key to trigger recomposition.
 * @param onEvent The action to be executed for each emitted event.
 */
@Composable
fun <T> ObserveAsEvents(
    events: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                events.collect(onEvent)
            }
        }
    }
}