package org.alba.hortus.di

import android.annotation.SuppressLint
import android.content.Context
import org.alba.hortus.data.local.AppDatabase
import org.alba.hortus.data.remote.GenerativeModel
import org.alba.hortus.data.remote.GenerativeModelAndroid
import org.alba.hortus.db.getDatabaseBuilder
import org.koin.dsl.module

@SuppressLint("StaticFieldLeak")
lateinit var androidContext: Context

actual fun platformModule() = module {
    single<AppDatabase> { getDatabaseBuilder(get()) }
    single<Context> { androidContext }
    single<GenerativeModel> { GenerativeModelAndroid() }
}