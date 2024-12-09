package org.alba.hortus

import androidx.compose.ui.window.ComposeUIViewController
import org.alba.hortus.data.remote.GenerativeModel
import org.alba.hortus.di.generativeModelIOS
import org.alba.hortus.di.initKoin
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController(generativeModel: GenerativeModel) = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    generativeModelIOS = generativeModel

    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle ==
                UIUserInterfaceStyle.UIUserInterfaceStyleDark
    App(
        darkTheme = isDarkTheme,
        dynamicColor = false,
    )
}