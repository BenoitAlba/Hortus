package org.alba.hortus

import android.app.Application
import org.alba.hortus.di.initKoin
import org.koin.core.component.KoinComponent

class HortusApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        initKoin {

        }
    }
}