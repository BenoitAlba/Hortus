package org.alba.hortus.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.alba.hortus.data.local.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getDatabaseBuilder(): AppDatabase {
    val dbFilePath = documentDirectory() + "/plant.db"
    return Room.databaseBuilder<AppDatabase>(
    name = dbFilePath,
    ).setDriver(BundledSQLiteDriver()).build()
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )

    return requireNotNull(documentDirectory?.path)
}
