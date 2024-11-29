package org.alba.hortus.di

import org.alba.hortus.data.local.AppDatabase
import org.alba.hortus.db.getDatabaseBuilder
import org.koin.dsl.module

actual fun platformModule() = module {
    single<AppDatabase> { getDatabaseBuilder(get()) }
}