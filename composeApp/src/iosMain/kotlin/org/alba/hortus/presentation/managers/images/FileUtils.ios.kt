package org.alba.hortus.presentation.managers.images

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import platform.Foundation.*
import kotlinx.cinterop.memScoped

@OptIn(BetaInteropApi::class)
actual fun saveByteArrayToFile(byteArray: ByteArray, fileName: String): String {
    val fileManager = NSFileManager.defaultManager
    val directory = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask).first() as NSURL
    val fileURL = directory.URLByAppendingPathComponent(fileName)

    fileManager.createFileAtPath(fileURL?.path!!, byteArray.toNSData(), null)
    return fileURL.path!!
}

@BetaInteropApi
@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}

@OptIn(ExperimentalForeignApi::class)
actual fun deleteFile(filePath: String): Boolean {
    val fileManager = NSFileManager.defaultManager()
    val fileUrl = NSURL.fileURLWithPath(filePath)
    try {
        fileManager.removeItemAtURL(fileUrl, error = null)
        return true
    } catch (e: Throwable) {
        return false
    }
}