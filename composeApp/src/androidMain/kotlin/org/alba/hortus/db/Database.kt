package org.alba.hortus.db

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

import org.alba.hortus.data.local.AppDatabase

fun getDatabaseBuilder(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath("plant.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}