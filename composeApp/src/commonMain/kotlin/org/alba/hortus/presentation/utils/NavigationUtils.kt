package org.alba.hortus.presentation.utils

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Fix the behavior of Voyager
 * https://github.com/adrielcafe/voyager/issues/437#issuecomment-2282658556
 */
fun Navigator.safeNavigate(coroutineScope: CoroutineScope, screen: Screen) {
    pop()
    coroutineScope.launch {
        delay(1)
        push(screen)
    }
}