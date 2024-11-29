package org.alba.hortus

import android.app.Application
import org.alba.hortus.di.initKoin
import org.koin.core.component.KoinComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class HortusApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@HortusApplication)
        }
    }
}