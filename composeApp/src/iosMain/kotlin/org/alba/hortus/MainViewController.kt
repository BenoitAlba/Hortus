package org.alba.hortus

import androidx.compose.ui.window.ComposeUIViewController
import org.alba.hortus.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }