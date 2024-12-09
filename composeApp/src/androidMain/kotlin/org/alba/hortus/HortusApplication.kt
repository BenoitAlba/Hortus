package org.alba.hortus

import android.app.Application
import com.google.firebase.FirebaseApp
import org.alba.hortus.di.androidContext
import org.alba.hortus.di.initKoin
import org.koin.core.component.KoinComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class HortusApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        androidContext = applicationContext
        initKoin {
            androidLogger()
            androidContext(this@HortusApplication)
        }
    }
}