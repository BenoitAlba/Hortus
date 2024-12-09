package org.alba.hortus.di

import org.alba.hortus.data.local.AppDatabase
import org.alba.hortus.data.remote.GenerativeModel
import org.alba.hortus.data.remote.GenerativeModelIOS
import org.alba.hortus.db.getDatabaseBuilder
import org.koin.dsl.module

var generativeModelIOS: GenerativeModel? = null


actual fun platformModule() = module {
    single<AppDatabase> { getDatabaseBuilder() }
    single<GenerativeModel> { GenerativeModelIOS() }
}