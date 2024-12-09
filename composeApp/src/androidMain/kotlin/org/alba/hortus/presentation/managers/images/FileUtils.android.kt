package org.alba.hortus.presentation.managers.images

import android.content.Context
import org.koin.java.KoinJavaComponent.get
import java.io.File

actual fun saveByteArrayToFile(byteArray: ByteArray, fileName: String): String {
   val context: Context = get(Context::class.java)
    val file = File(context.filesDir, fileName)
    file.writeBytes(byteArray)
    return file.absolutePath
}

actual fun deleteFile(fileName: String): Boolean {
    return true
}