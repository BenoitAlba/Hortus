package org.alba.hortus.presentation.managers.images

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import java.io.ByteArrayOutputStream

actual class SharedImage(private val bitmap: Bitmap?) {
    actual fun toByteArray(): ByteArray? {
        return if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            @Suppress("MagicNumber") bitmap.compress(
                android.graphics.Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream
            )
            byteArrayOutputStream.toByteArray()
        } else {
            println("toByteArray null")
            null
        }
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return if (byteArray != null) {
            return byteArray.toImageBitmap()
        } else {
            println("toImageBitmap null")
            null
        }
    }
}